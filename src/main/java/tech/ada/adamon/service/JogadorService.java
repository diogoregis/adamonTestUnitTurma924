package tech.ada.adamon.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ada.adamon.dto.SalvarJogadorDTO;
import tech.ada.adamon.dto.converter.JogadorDtoConverter;
import tech.ada.adamon.model.Adamon;
import tech.ada.adamon.model.Jogador;
import tech.ada.adamon.repository.JogadorRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class JogadorService {

    @Autowired
    private JogadorRepository jogadorRepository;



    public void batalhar(Jogador jogador1, Jogador jogador2) {
        int adamonJogador1Conta = 0, adamonJogador2Conta = 0;


    }


    public void comprarAdamon(Jogador jogador, Adamon adamon) {
        List<Adamon> equipeAdamonJogador = jogador.getAdamons();
        BigDecimal saldoAtual = jogador.getSaldo();
        BigDecimal precoAdamon = adamon.obterPreco();

        boolean possuiSaldoSuficiente = saldoAtual.compareTo(precoAdamon) > 0;
        boolean possuiEspacoNaEquipe = equipeAdamonJogador.size() < 6;

        if (possuiEspacoNaEquipe && possuiSaldoSuficiente) {
            equipeAdamonJogador.add(adamon);
            jogador.setSaldo(saldoAtual.subtract(precoAdamon));
            atualizarJogador(jogador, jogador.getId());
        } else if (!possuiSaldoSuficiente) {
            throw new RuntimeException("Não possui saldo suficiente");
        } else if (!possuiEspacoNaEquipe) {
            throw new RuntimeException("Não possui espaço na equipe");
        }
    }

    public void venderAdamon(Jogador comprador, Adamon adamon) {

    }

    public void atualizarJogador(Jogador jogador, Long idJogador) {
        encontrarJogadorPorId(idJogador);
        jogador.setId(idJogador);
        jogadorRepository.save(jogador);
    }

    public Jogador encontrarJogadorPorId(Long idJogador) {
        Optional<Jogador> optionalJogador = jogadorRepository.findById(idJogador);
        return optionalJogador
                .orElseThrow(() -> new RuntimeException("Não encontrado jogador com ID: " + idJogador));
    }

    public Jogador salvarJogador(SalvarJogadorDTO dto) {
        return jogadorRepository.save(JogadorDtoConverter.converterDto(dto));
    }

    public int lutaEntreAdamons(Adamon adamon1, Adamon adamon2){
        int danoCausado = 0;
        while ((adamon1.getVida() > 0) && adamon2.getVida() > 0){

            // QUEM TEM MAIOR VELOCIDADE ATAK PRIMEIRO
            // PRIMEIRO ATAQUE:
            if(adamon1.getVelocidade() >= adamon2.getVelocidade()){
                if(adamon1.getAtaque() > adamon2.getAtaque()){
                    danoCausado = adamon1.getAtaque() - adamon2.getDefesa();
                    adamon2.setVida(adamon2.getVida() - danoCausado);
                } else adamon2.setVida(adamon2.getVida() - 1);
            }
            if(adamon2.getVida() <= 0){
                return 1;
            }
            // SEGUNDO ATAQUE
            if(adamon1.getVelocidade() <= adamon2.getVelocidade()){
                if(adamon2.getAtaque() > adamon1.getAtaque()){
                    danoCausado = adamon2.getAtaque() - adamon1.getDefesa();
                    adamon1.setVida(adamon1.getVida() - danoCausado);
                } else adamon1.setVida(adamon1.getVida() - 1);
            }
            if (adamon1.getVida() <=0){
                return 2;
            }
        }
        return -1;
    }

}
