# BlockyHelp

BlockyHelp é um plugin para o servidor BlockyCRAFT que exibe uma lista personalizada de comandos e informações diretamente no servidor, com sistema de páginas automática e visual integrado aos demais plugins Blocky.

## Como Funciona

1. Adicione o plugin `BlockyHelp.jar` à pasta plugins do seu servidor.
2. Ao iniciar o servidor, o arquivo `config.properties` será gerado automaticamente na pasta do plugin, pronto para edição.
3. Edite `config.properties`, adicionando/removendo comandos conforme desejar. Cada comando deve seguir o padrão: `command.X.name=§b/comando §7- §fDescrição do comando`
4. Use `/ajuda` no chat do servidor para exibir a lista, que será paginada conforme o número de comandos.
5. Use `/ajuda <número>` para navegar entre páginas de comandos.

## Configuração

O arquivo `config.properties` permite definir:

- Cores, header e footer da janela de ajuda.
- Lista de comandos e descrições.
- Número de comandos por página (`pagination.max`).
- Mensagens de navegação e erro, totalmente customizáveis.

## Reportar bugs ou requisitar features
Reporte bugs ou sugira novas funcionalidades na seção [Issues](https://github.com/andradecore/BlockyHelp/issues) do projeto.

## Contato:
- Discord: https://discord.gg/tthPMHrP