# VisionProject

Aplicativo Android desenvolvido em Java no contexto da disciplina de **Visão Computacional**, com foco na captura e no processamento básico de imagens em dispositivos móveis.

## Finalidade do projeto

O projeto foi desenvolvido com o objetivo de implementar uma aplicação Android capaz de:

- inicializar a câmera traseira do dispositivo;
- exibir o fluxo de imagem em tempo real na interface;
- capturar o frame atual sob demanda do usuário;
- salvar a imagem capturada;
- aplicar uma pipeline básica de processamento de imagens;
- converter a imagem para tons de cinza;
- aplicar suavização com filtro Gaussiano;
- detectar bordas utilizando o algoritmo de Canny;
- permitir o ajuste dos limiares do Canny em tempo real por meio de controles deslizantes;
- salvar as imagens resultantes do processamento.

## Contexto acadêmico

Este projeto integra atividades práticas da disciplina de **Visão Computacional**, voltadas à familiarização com:

- a estrutura básica de um projeto Android;
- o uso do Android Studio como ambiente de desenvolvimento;
- a integração da biblioteca CameraX;
- a integração da biblioteca OpenCV em aplicações Android;
- operações iniciais de processamento digital de imagens;
- análise do efeito dos limiares na detecção de bordas.

## Tecnologias e componentes utilizados

- Android Studio
- Java
- CameraX
- OpenCV
- Gradle
- PreviewView para exibição do fluxo da câmera
- ImageCapture para captura e salvamento da imagem
- SeekBar para ajuste dos limiares do detector de bordas
- ImageView para exibição da imagem processada

## Funcionalidades implementadas

Até o estágio atual de desenvolvimento, foram implementadas as seguintes funcionalidades:

- abertura da câmera traseira do dispositivo;
- exibição do preview da câmera em tempo real;
- captura de imagem por interação do usuário;
- salvamento local da imagem capturada;
- carregamento da última imagem capturada para processamento;
- conversão da imagem para escala de cinza;
- aplicação de filtro Gaussiano para suavização;
- detecção de bordas com Canny;
- ajuste em tempo real dos limiares do Canny;
- exibição da imagem processada na interface;
- salvamento de quatro imagens na galeria:
  - imagem original;
  - imagem em tons de cinza;
  - imagem suavizada;
  - imagem com bordas detectadas.

## Estrutura do projeto

A organização principal do projeto é composta pelos seguintes elementos:

- `app/` – módulo principal da aplicação;
- `src/main/java/` – código-fonte da aplicação;
- `src/main/res/layout/` – arquivos de interface;
- `build.gradle.kts` – configuração principal do módulo;
- `settings.gradle.kts` – definição da estrutura de módulos;
- `gradlew` e `gradlew.bat` – scripts de execução do Gradle Wrapper;
- `.gitignore` – definição dos arquivos e diretórios não versionados.

## Procedimento de execução

Para executar o projeto, recomenda-se o seguinte fluxo:

1. Abrir o projeto no Android Studio;
2. Aguardar a sincronização completa das dependências Gradle;
3. Conectar um dispositivo Android com depuração USB habilitada, ou iniciar um emulador compatível;
4. Executar a aplicação;
5. Conceder as permissões necessárias de câmera;
6. Utilizar o botão **Capturar** para registrar a imagem;
7. Ajustar os limiares do detector de bordas com os controles deslizantes;
8. Utilizar o botão **Processar** para executar a pipeline de processamento e salvar os resultados.

## Pipeline de processamento implementada

A pipeline utilizada no aplicativo segue a sequência:

1. captura da imagem;
2. conversão para escala de cinza;
3. aplicação de filtro Gaussiano;
4. detecção de bordas com Canny;
5. exibição do resultado;
6. salvamento das imagens processadas.

## Observações

Este projeto representa uma etapa introdutória, porém fundamental, para o desenvolvimento de aplicações mais avançadas em visão computacional embarcada em dispositivos móveis. A implementação realizada estabelece base para futuras extensões, como:

- segmentação de objetos;
- extração de características;
- mensuração corporal de animais;
- detecção automática de contornos;
- integração com métodos mais avançados de análise de imagens.

Durante os testes desta etapa, foi utilizada uma imagem de **cachorro** como objeto de validação do pipeline, embora o objetivo futuro do projeto seja a aplicação em **equinos** para extração de medidas corporais.

## Apoio no desenvolvimento

O desenvolvimento contou com apoio do **ChatGPT** como ferramenta auxiliar para orientação técnica, organização do fluxo de implementação, correção de trechos de código e estruturação do relatório acadêmico.

## Autor

Projeto desenvolvido por **Alan Freire** no âmbito das atividades acadêmicas da disciplina de **Visão Computacional**.