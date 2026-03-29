# VisionProject

## Descrição

O **VisionProject** consiste em uma aplicação Android desenvolvida em **Java**, no contexto da disciplina de **Visão Computacional**, com o propósito de integrar captura de imagens em dispositivos móveis e operações iniciais de processamento digital de imagens.

A aplicação foi estruturada como continuidade de uma atividade prática introdutória, inicialmente voltada à configuração do ambiente e à captura de imagens com a câmera traseira do dispositivo, sendo posteriormente estendida para contemplar uma pipeline básica de processamento com a biblioteca **OpenCV**.

## Objetivo

O objetivo do projeto é implementar uma aplicação Android capaz de:

- inicializar a câmera traseira do dispositivo;
- exibir o fluxo de imagem em tempo real na interface;
- capturar imagens sob demanda do usuário;
- aplicar operações básicas de processamento digital de imagens;
- converter a imagem capturada para escala de cinza;
- aplicar suavização com filtro Gaussiano;
- detectar bordas por meio do algoritmo de **Canny**;
- permitir o ajuste interativo dos limiares do detector de bordas;
- salvar os resultados do processamento para posterior análise.

## Contextualização acadêmica

O desenvolvimento deste projeto está inserido no processo de aprendizagem de fundamentos de **visão computacional aplicada a dispositivos móveis**, envolvendo:

- estruturação de projetos Android no **Android Studio**;
- integração da biblioteca **CameraX**;
- utilização da biblioteca **OpenCV** em ambiente Android;
- compreensão prática de conceitos como:
  - aquisição de imagens;
  - conversão para tons de cinza;
  - suavização por filtro Gaussiano;
  - detecção de bordas;
  - influência dos limiares na sensibilidade do algoritmo.

A proposta da atividade buscou relacionar a implementação prática com conceitos teóricos de amostragem, quantização, filtragem e realce de características estruturais em imagens digitais.

## Tecnologias utilizadas

O projeto foi desenvolvido com os seguintes recursos:

- **Android Studio**
- **Java**
- **Gradle**
- **CameraX**
- **OpenCV**
- **PreviewView**
- **ImageCapture**
- **SeekBar**
- **ImageView**

## Funcionalidades implementadas

Na versão atual, a aplicação contempla as seguintes funcionalidades:

- abertura e inicialização da câmera traseira do dispositivo;
- exibição do preview em tempo real;
- captura de imagem por ação do usuário;
- armazenamento da imagem capturada;
- carregamento da imagem para processamento;
- conversão da imagem original para escala de cinza;
- aplicação de filtro Gaussiano para suavização;
- detecção de bordas com o algoritmo de Canny;
- ajuste dinâmico dos limiares do detector por meio de controles deslizantes;
- exibição da imagem processada na interface;
- salvamento de quatro versões da imagem:
  - original;
  - em tons de cinza;
  - suavizada;
  - com bordas detectadas.

## Pipeline de processamento

A pipeline implementada na aplicação segue a sequência descrita abaixo:

1. captura da imagem original;
2. conversão para escala de cinza;
3. aplicação de filtro Gaussiano;
4. detecção de bordas com Canny;
5. exibição do resultado processado;
6. salvamento das imagens geradas.

## Estrutura geral do projeto

A organização do repositório está centrada nos seguintes componentes:

- `app/` — módulo principal da aplicação;
- `app/src/main/java/` — código-fonte em Java;
- `app/src/main/res/layout/` — arquivos de interface gráfica;
- `build.gradle.kts` — configuração de dependências e build;
- `settings.gradle.kts` — definição estrutural do projeto;
- `gradlew` e `gradlew.bat` — scripts do Gradle Wrapper;
- `.gitignore` — definição de arquivos e diretórios não versionados.

## Procedimento de execução

Para executar o projeto, recomenda-se o seguinte procedimento:

1. abrir o projeto no **Android Studio**;
2. aguardar a sincronização completa das dependências Gradle;
3. conectar um dispositivo Android com depuração USB habilitada, ou utilizar um emulador compatível;
4. executar a aplicação;
5. conceder as permissões necessárias de acesso à câmera;
6. utilizar o botão **Capturar** para registrar a imagem;
7. ajustar os limiares do detector de bordas conforme necessário;
8. utilizar o botão **Processar** para executar a pipeline de processamento;
9. verificar o salvamento das imagens processadas no dispositivo.

## Resultados observados

Durante os testes realizados, verificou-se que o comportamento do detector de bordas Canny apresentou sensibilidade significativa em relação aos limiares selecionados.

De modo geral:

- limiares mais baixos produziram maior quantidade de ruído, resultando na detecção de bordas falsas;
- limiares mais elevados reduziram a presença de ruído, porém também ocasionaram perda de bordas relevantes;
- valores intermediários forneceram resultados mais equilibrados, preservando melhor as estruturas principais da imagem.

Essas observações são coerentes com o comportamento teórico do algoritmo, uma vez que a escolha dos limiares influencia diretamente o equilíbrio entre sensibilidade e robustez da detecção.

## Observações experimentais

Embora o objetivo futuro do projeto esteja associado à **extração de medidas corporais em equinos**, nesta etapa foi utilizada uma **imagem do meu cachorro (Juca)** para validação do pipeline de processamento.

Tal escolha não compromete a atividade, uma vez que o foco da prática esteve na implementação e verificação do funcionamento do fluxo de captura, filtragem, detecção de bordas e salvamento das imagens resultantes.

## Perspectivas futuras

A implementação atual estabelece base para extensões posteriores, tais como:

- segmentação de objetos de interesse;
- extração de contornos com maior robustez;
- mensuração corporal automatizada;
- identificação de pontos anatômicos;
- desenvolvimento de métodos aplicados à avaliação morfométrica de animais.

## Apoio no desenvolvimento

O desenvolvimento desta atividade contou com o apoio do **ChatGPT** como ferramenta auxiliar de suporte técnico e organização do processo de implementação, sendo utilizado para:

- esclarecimento de dúvidas sobre configuração do projeto;
- apoio na integração entre **CameraX** e **OpenCV**;
- orientação na construção da interface;
- suporte na estruturação do código;
- auxílio na redação e organização textual do relatório.

O uso da ferramenta ocorreu como suporte complementar ao processo de aprendizagem, sem substituir a execução prática, os testes realizados e a interpretação dos resultados obtidos.

## Autor

**Alan Freire**  
Projeto desenvolvido no âmbito das atividades acadêmicas da disciplina de **Visão Computacional**.