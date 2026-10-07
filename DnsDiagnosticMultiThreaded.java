// Importação das classes necessárias para manipulação de endereços IP e suporte a concorrência

import java.net.InetAddress;

import java.net.UnknownHostException;

import java.util.Arrays;

import java.util.List;

import java.util.concurrent.Callable;

import java.util.concurrent.ExecutorService;

import java.util.concurrent.Executors;

import java.util.concurrent.Future;

/**

 * Classe principal responsável pelo Diagnóstico DNS Multi-Threaded.

 * Demonstra a utilização avançada de InetAddress de forma concorrente.

 */

public class DnsDiagnosticMultiThreaded {

    // Define o tempo limite de alcance (timeout) em milissegundos para o teste de ping/isReachable

    private static final int TIMEOUT_MS = 2000;

    // Define o número de threads que trabalharão em paralelo no pool de execução

    private static final int THREAD_POOL_SIZE = 4;

    public static void main(String[] args) {

        // Exibe o cabeçalho descritivo do utilitário no terminal

        System.out.println("================================================================================");

        System.out.println("       CASO DE ESTUDO 1: DIAGNÓSTICO DNS E ALCANÇABILIDADE MULTI-THREADED       ");

        System.out.println("================================================================================\n");

        // Lista de domínios de teste para diagnóstico de rede

        List<String> dominios = Arrays.asList(

            "google.com",

            "uzambeze.ac.mz",

            "github.com",

            "dominio.invalido.inexistente.test",

            "cloudflare.com"

        );

        // Criação de um ExecutorService com um pool fixo de threads para processamento paralelo

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try {

            // Mapeia cada domínio para uma tarefa concorrente (Callable) e submete ao executor

            List<Callable<String>> tarefas = dominios.stream()

                .map(dominio -> (Callable<String>) () -> diagnosticarDominio(dominio))

                .toList();

            // Executa todas as tarefas simultaneamente e aguarda a conclusão de todas as Futures

            List<Future<String>> resultados = executor.invokeAll(tarefas);

            // Itera e exibe o resultado retornado por cada thread finalizada

            for (Future<String> resultado : resultados) {

                System.out.println(resultado.get());

                System.out.println("--------------------------------------------------------------------------------");

            }

        } catch (Exception e) {

            // Captura falhas de interrupção ou execução de tarefas no nível do executor

            System.err.println("Erro crítico na execução do diagnóstico: " + e.getMessage());

        } finally {

            // Encerra adequadamente o pool de threads para liberar recursos do sistema operacional

            executor.shutdown();

            System.out.println("Diagnóstico concluído. Pool de threads encerrado com sucesso.");

        }

    }

    /**

     * Executa a resolução DNS e teste de alcançabilidade para um único domínio.

     * 

     * @param dominio O nome de domínio a ser diagnosticado.

     * @return Uma String formatada com os detalhes do diagnóstico.

     */

    private static String diagnosticarDominio(String dominio) {

        StringBuilder sb = new StringBuilder();

        // Registra o nome da thread atual que está executando esta tarefa específica

        sb.append("Thread [").append(Thread.currentThread().getName()).append("] - Domínio: ").append(dominio).append("\n");

        try {

            // Resolve todos os endereços IP associados ao domínio (IPv4 e IPv6)

            InetAddress[] enderecos = InetAddress.getAllByName(dominio);

            sb.append("  ► Total de endereços encontrados: ").append(enderecos.length).append("\n");

            // Itera sobre cada endereço IP retornado pelo servidor DNS

            for (InetAddress addr : enderecos) {

                sb.append("    • IP: ").append(addr.getHostAddress());

                sb.append(" | Canonical Hostname: ").append(addr.getCanonicalHostName());

                // Testa se o endereço IP responde no limite de tempo especificado

                boolean alcancavel = addr.isReachable(TIMEOUT_MS);

                sb.append(" | Alcançável: ").append(alcancavel ? "SIM" : "NÃO (Timeout/Bloqueado)");

                sb.append("\n");

            }

        } catch (UnknownHostException e) {

            // Captura a exceção disparada quando o nome de domínio não pode ser resolvido pelo DNS

            sb.append("  ✖ ERRO: Domínio não encontrado ou falha na resolução DNS (UnknownHostException).\n");

        } catch (Exception e) {

            // Captura erros genéricos de I/O ou permissão de rede durante a execução

            sb.append("  ✖ ERRO INESPERADO: ").append(e.getMessage()).append("\n");

        }

        return sb.toString();

    }

}
