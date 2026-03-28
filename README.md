# VisionProject

Projeto desenvolvido como parte da disciplina de Visão Computacional.

## Objetivo

Desenvolver um aplicativo Android em Java capaz de:

- abrir a câmera traseira do dispositivo;
- exibir o preview da câmera em tempo real;
- capturar o frame atual;
- salvar a imagem capturada em formato JPEG no armazenamento interno.

## Tecnologias utilizadas

- Android Studio
- Java
- CameraX
- Gradle

## Estrutura do projeto

- `app/` – código-fonte principal do aplicativo
- `gradle/` – arquivos de configuração do Gradle
- `build.gradle.kts` – configuração principal do projeto
- `settings.gradle.kts` – definição dos módulos do projeto

## Funcionalidades implementadas

- inicialização da câmera traseira;
- visualização em tempo real via `PreviewView`;
- botão para captura da imagem;
- salvamento local da imagem capturada.

## Execução

1. Abrir o projeto no Android Studio.
2. Aguardar o carregamento das dependências Gradle.
3. Conectar um dispositivo Android com depuração USB ativada ou iniciar um emulador.
4. Executar o aplicativo.
5. Permitir acesso à câmera, se solicitado.
6. Utilizar o botão de captura para salvar a imagem.

## Observação

Este projeto foi desenvolvido para fins acadêmicos, com foco na configuração do ambiente Android e na implementação inicial de captura de imagem em aplicações de visão computacional.