# VisionProject

Aplicativo Android desenvolvido em Java no contexto da disciplina de Visão Computacional, com foco na configuração do ambiente de desenvolvimento e na implementação inicial de captura de imagens por meio da câmera traseira do dispositivo.

## Finalidade do projeto

O projeto foi desenvolvido com o objetivo de implementar uma aplicação Android capaz de:

- inicializar a câmera traseira do dispositivo;
- exibir o fluxo de imagem em tempo real na interface;
- capturar o frame atual sob demanda do usuário;
- salvar a imagem capturada em formato JPEG no armazenamento interno.

## Contexto acadêmico

Este projeto integra uma atividade prática voltada à familiarização com:

- a estrutura básica de um projeto Android;
- o uso do Android Studio como ambiente de desenvolvimento;
- a integração da biblioteca CameraX;
- a manipulação inicial de imagens em aplicações voltadas à visão computacional.

## Tecnologias e componentes utilizados

- **Android Studio**
- **Java**
- **CameraX**
- **Gradle**
- **PreviewView** para exibição do fluxo da câmera
- **ImageCapture** para registro e salvamento da imagem

## Estrutura do projeto

A organização principal do projeto é composta pelos seguintes elementos:

- `app/` – módulo principal da aplicação;
- `gradle/` – arquivos auxiliares de configuração do Gradle;
- `build.gradle.kts` – configuração principal do projeto;
- `settings.gradle.kts` – definição da estrutura de módulos;
- `gradlew` e `gradlew.bat` – scripts de execução do Gradle Wrapper;
- `.gitignore` – definição dos arquivos e diretórios não versionados.

## Funcionalidades implementadas

Até o estágio atual de desenvolvimento, foram implementadas as seguintes funcionalidades:

- abertura da câmera traseira do dispositivo;
- exibição do preview da câmera em tempo real;
- captura de imagem por interação do usuário;
- salvamento local da imagem capturada em formato JPEG.

## Procedimento de execução

Para executar o projeto, recomenda-se o seguinte fluxo:

1. Abrir o projeto no Android Studio;
2. Aguardar a sincronização completa das dependências Gradle;
3. Conectar um dispositivo Android com depuração USB habilitada, ou iniciar um emulador compatível;
4. Executar a aplicação;
5. Conceder as permissões necessárias de câmera;
6. Utilizar o botão de captura para registrar e salvar a imagem.

## Observações

Este projeto representa uma etapa introdutória, porém fundamental, para o desenvolvimento de aplicações mais avançadas em visão computacional embarcada em dispositivos móveis. A implementação realizada estabelece a base para futuras extensões, como processamento de imagens, extração de características, detecção de objetos e integração com bibliotecas especializadas.

## Autor

Projeto desenvolvido por **Alan Freire** no âmbito das atividades acadêmicas da disciplina de **Visão Computacional**.