package jogodaforca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class JogoDaForca extends JFrame {
    private static final String[] CATEGORIAS = { "Animais", "Objetos", "Nomes", "Cidades", "Frutas" };
    private static final String[][] PALAVRAS = {
            { "cachorro", "gato", "leão", "elefante", "tigre", "girafa", "macaco", "cavalo", "coelho", "rato" },
            { "cadeira", "mesa", "lápis", "caneta", "sofá", "computador", "carro", "livro", "telefone", "garrafa" },
            { "ana", "maria", "pedro", "jose", "lucas", "joao", "carlos", "laura", "miguel", "andre" },
            { "sao paulo", "rio de janeiro", "brasilia", "salvador", "manaus", "recife", "fortaleza", "curitiba", "porto alegre", "belo horizonte" },
            { "maça", "banana", "uva", "morango", "laranja", "abacaxi", "manga", "melancia", "pera", "limão" }
    };

    private String categoria;
    private String palavraSelecionada;
    private String palavraEmExibicao;
    private int vidas;

    private JLabel categoriaLabel;
    private JLabel palavraLabel;
    private JLabel vidasLabel;
    private JTextField letraTextField;
    private JButton tentarButton;

    public JogoDaForca() {
        super("Jogo da Forca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        categoriaLabel = new JLabel("Escolha uma categoria:");
        categoriaLabel.setHorizontalAlignment(JLabel.CENTER);

        palavraLabel = new JLabel();
        palavraLabel.setHorizontalAlignment(JLabel.CENTER);
        palavraLabel.setFont(new Font("Courier New", Font.BOLD, 24));

        vidasLabel = new JLabel();
        vidasLabel.setHorizontalAlignment(JLabel.CENTER);

        letraTextField = new JTextField(10);

        tentarButton = new JButton("Tentar");
        tentarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!acabou()) {
                    char letra = letraTextField.getText().toUpperCase().charAt(0);
                    boolean acertou = tentarLetra(letra);
                    letraTextField.setText("");

                    if (!acertou) {
                        JOptionPane.showMessageDialog(JogoDaForca.this, "Letra incorreta! Você perdeu uma vida.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }

                    atualizarInterface();

                    if (acabou()) {
                        if (vidas == 0) {
                            JOptionPane.showMessageDialog(JogoDaForca.this, "Você perdeu! A palavra era: " + palavraSelecionada, "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(JogoDaForca.this, "Parabéns! Você ganhou!", "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
                        }

                        reiniciarJogo();
                    }
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(categoriaLabel, BorderLayout.NORTH);
        panel.add(palavraLabel, BorderLayout.CENTER);
        panel.add(vidasLabel, BorderLayout.SOUTH);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Digite uma letra:"));
        inputPanel.add(letraTextField);
        inputPanel.add(tentarButton);

        add(panel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        mostrarTelaInicial();
    }

    private void mostrarTelaInicial() {
        Object[] categorias = { "Animais", "Objetos", "Nomes", "Cidades", "Frutas" };
        int categoriaIndex = JOptionPane.showOptionDialog(JogoDaForca.this, "Escolha uma categoria:", "Jogo da Forca", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, categorias, null);

        if (categoriaIndex != JOptionPane.CLOSED_OPTION) {
            selecionarCategoria(categoriaIndex);
            atualizarInterface();
        } else {
            System.exit(0);
        }
    }

    private void selecionarCategoria(int categoriaIndex) {
        if (categoriaIndex >= 0 && categoriaIndex < CATEGORIAS.length) {
            categoria = CATEGORIAS[categoriaIndex];
            selecionarPalavra();
        }
    }

    private void selecionarPalavra() {
    Random random = new Random();
    int categoriaIndex = getCategoriaIndex();

    if (categoriaIndex != -1) {
        int palavraIndex = random.nextInt(PALAVRAS[categoriaIndex].length);
        palavraSelecionada = PALAVRAS[categoriaIndex][palavraIndex];
        palavraEmExibicao = "_".repeat(palavraSelecionada.length());
        vidas = 5;
    }
}

private int getCategoriaIndex() {
    for (int i = 0; i < CATEGORIAS.length; i++) {
        if (CATEGORIAS[i].equalsIgnoreCase(categoria)) {
            return i;
        }
    }
    return -1; // Categoria não encontrada
}


   private boolean tentarLetra(char letra) {
    boolean acertou = false;
    StringBuilder sb = new StringBuilder(palavraEmExibicao);

    char letraMinuscula = Character.toLowerCase(letra);

    for (int i = 0; i < palavraSelecionada.length(); i++) {
        if (Character.toLowerCase(palavraSelecionada.charAt(i)) == letraMinuscula) {
            sb.setCharAt(i, letraMinuscula);
            acertou = true;
        }
    }

    palavraEmExibicao = sb.toString();
    if (!acertou) {
        vidas--;
    }

    return acertou;
}

    private void atualizarInterface() {
    categoriaLabel.setText("Categoria: " + categoria);

    // Adicionar espaçamento entre os traços da palavra
    StringBuilder palavraExibicaoComEspacos = new StringBuilder();
    for (int i = 0; i < palavraEmExibicao.length(); i++) {
        palavraExibicaoComEspacos.append(palavraEmExibicao.charAt(i)).append(" ");
    }

    palavraLabel.setText(palavraExibicaoComEspacos.toString());
    vidasLabel.setText("Vidas restantes: " + vidas);
}


    private boolean acabou() {
        return vidas == 0 || palavraEmExibicao.equals(palavraSelecionada);
    }

    private void reiniciarJogo() {
    UIManager.put("OptionPane.yesButtonText", "Sim");
    UIManager.put("OptionPane.noButtonText", "Não");

    int resposta = JOptionPane.showConfirmDialog(JogoDaForca.this, "Deseja jogar novamente?", "Jogo da Forca", JOptionPane.YES_NO_OPTION);
    if (resposta == JOptionPane.YES_OPTION) {
        mostrarTelaInicial();
    } else {
        System.exit(0);
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JogoDaForca().setVisible(true);
            }
        });
    }
}
