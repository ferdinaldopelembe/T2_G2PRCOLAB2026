
// Importações necessárias para abstração de URI, URL, InetAddress e tratamento de exceções de rede

import java.net.InetAddress;

import java.net.MalformedURLException;

import java.net.URI;

import java.net.URISyntaxException;

import java.net.UnknownHostException;

import java.util.Arrays;

import java.util.List;

/**

 * Módulo de Análise Sintática e Resolução de Recursos da Internet.

 * Utiliza a abordagem moderna (java.net.URI) para parsing e validação rigorosa.

 */

public class ResourceIdentifierAnalyzer {

    public static void main(String[] args) {

        // Exibe o cabeçalho descritivo no terminal

        System.out.println("================================================================================");

        System.out.println("    CASO DE ESTUDO 2: ANÁLISE E DESCONSTRUÇÃO ESTRUTURAL DE URIs E URLs         ");

        System.out.println("================================================================================\n");

        // Conjunto variado de cenários de teste representando diferentes formatos de identificadores

        List<String> entradasTeste = Arrays.asList(

            "https://www.uzambeze.ac.mz:443/portal/estudante?id=12345#notas",

            "http://192.168.1.254/admin/config.html",

            "mailto:suporte.academico@uzambeze.ac.mz",

            "urn:isbn:0451450523",

            "ftp://usuario:senha123@ftp.uzambeze.ac.mz:21/files/docs.zip",

            "htt ps://url_invalida_com_espaco.com/test", // URI Sintaticamente Inválida

            "https://dominio-inexistente-uzambeze-test.mz/" // URI Válida, mas Host Inexistente

        );

        // Contador para identificação sequencial dos testes no relatório

        int cenarioIndex = 1;

        // Processa sequencialmente cada entrada de teste

        for (String entrada : entradasTeste) {

            System.out.println(">>> CENÁRIO DE TESTE " + cenarioIndex++ + ": [" + entrada + "]");

            analisarEProcessarRecurso(entrada);

            System.out.println("--------------------------------------------------------------------------------\n");

        }

    }

    /**

     * Realiza o parsing, decomposição estrutural e resolução de rede da entrada fornecida.

     * 

     * @param rawInput String representando o identificador a ser analisado.

     */

    private static void analisarEProcessarRecurso(String rawInput) {

        try {

            // PASSO 1: Parsing e Validação Sintática com a Abordagem Moderna (java.net.URI)

            // Lança URISyntaxException se a string violar as regras da RFC 3986

            URI uri = new URI(rawInput);

            System.out.println("  [PARSING SINTÁTICO URI]: Sucesso!");

            System.out.println("    • É Absoluta? .....: " + uri.isAbsolute());

            System.out.println("    • É Oposta/Opaque? : " + uri.isOpaque());

            System.out.println("    • Esquema (Scheme) : " + uri.getScheme());

            System.out.println("    • Autoridade ......: " + uri.getAuthority());

            System.out.println("    • User Info .......: " + uri.getUserInfo());

            System.out.println("    • Host ............: " + uri.getHost());

            System.out.println("    • Porta ...........: " + (uri.getPort() == -1 ? "Padrão / Não definida" : uri.getPort()));

            System.out.println("    • Caminho (Path) ..: " + uri.getPath());

            System.out.println("    • Consulta (Query) : " + uri.getQuery());

            System.out.println("    • Fragmento .......: " + uri.getFragment());

            // PASSO 2: Avaliar se o recurso possui características de localizador de rede (URL)

            if (uri.isAbsolute() && (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https") || uri.getScheme().equalsIgnoreCase("ftp"))) {

                System.out.println("  [RECONHECIMENTO DE TIPOLOGIA]: O recurso é um Localizador de Rede (URL).");

                // PASSO 3: Conversão Explícita e Segura de URI para URL (Boa Prática Java 20+)

                try {

                    java.net.URL url = uri.toURL();

                    System.out.println("    • Conversão uri.toURL() realizada com sucesso.");

                    // PASSO 4: Resolução de Rede / DNS a partir do Host extraído

                    if (uri.getHost() != null) {

                        System.out.println("  [RESOLUÇÃO DNS DO HOST]:");

                        InetAddress address = InetAddress.getByName(uri.getHost());

                        System.out.println("    • IP Resolvido ....: " + address.getHostAddress());

                        System.out.println("    • Nome Canónico ...: " + address.getCanonicalHostName());

                    }

                } catch (MalformedURLException e) {

                    System.out.println("    ✖ ERRO NA CONVERSÃO PARA URL: Esquema não suportado para I/O: " + e.getMessage());

                } catch (UnknownHostException e) {

                    System.out.println("    ✖ FALHA NA RESOLUÇÃO DNS: Host não pode ser resolvido (" + e.getMessage() + ")");

                }

            } else if (uri.getScheme() != null && uri.getScheme().equalsIgnoreCase("urn")) {

                System.out.println("  [RECONHECIMENTO DE TIPOLOGIA]: O recurso é um Nome Único de Recurso (URN). Não aplicável para I/O de rede.");

            } else {

                System.out.println("  [RECONHECIMENTO DE TIPOLOGIA]: URI Genérica / Não aplicável para conexão I/O direta.");

            }

        } catch (URISyntaxException e) {

            // Captura falhas de parsing sintático de forma isolada sem travar o sistema

            System.out.println("  ✖ ERRO DE SINTAXE (URISyntaxException): Entrada inválida conforme a RFC 3986.");

            System.out.println("    • Detalhe do Erro : " + e.getReason());

            System.out.println("    • Posição do Erro : " + e.getIndex());

            System.out.println("    • String Malformada: " + e.getInput());

        } catch (IllegalArgumentException e) {

            System.out.println("  ✖ ERRO DE ARGUMENTO: " + e.getMessage());

        }

    }

}
