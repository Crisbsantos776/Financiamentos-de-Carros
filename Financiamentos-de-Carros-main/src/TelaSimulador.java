import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TelaSimulador extends JFrame {

    //campos do veículo
    JComboBox<String> cbMarca;
    JTextField txtModelo;
    JComboBox<Integer> cbAno;
    JTextField txtValor;

    //painel do veículo novo e usado
    JRadioButton rbNovo;
    JRadioButton rbUsado;

    //painel do veículo usado
    JPanel painelUsado;
    JTextField txtKm;
    JTextField txtDonos;

    //financiamento
    JCheckBox chkEntrada;
    JLabel lblEntrada;
    JTextField txtEntrada;
    JComboBox<String> cbParcelas;

    //botões
    JButton btnLimpar;
    JButton btnCalcular;
    JPanel painelBotoes;

    //resultado
    JPanel painelResultado;
    JLabel lblFinanciado;
    JLabel lblParcela;
    JLabel lblTotal;

    JPanel painelPrincipal;

    //vitrine da marca escolhida
    JPanel painelMarca;
    JLabel lblFoto;
    JLabel lblApelido;
    JLabel lblCabecalho;

    //painéis que trocam de cor junto com a marca
    List<JPanel> painelsTematicos = new ArrayList<>();

    static final int LARGURA_FOTO = 300;
    static final int ALTURA_FOTO = 220;

    //formata número no padrão brasileiro
    NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    //formata numero sem o "R$", usado dentro dos campos de texto enquanto a pessoa digita
    NumberFormat formatoNumero = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

    public TelaSimulador() {
        setTitle("Simulador de Financiamento de Veiculos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //o valor digitado sempre mostra duas casas decimais
        formatoNumero.setMinimumFractionDigits(2);
        formatoNumero.setMaximumFractionDigits(2);

        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));

        painelPrincipal.add(criarPainelVeiculo());
        painelPrincipal.add(criarPainelTipo());
        painelPrincipal.add(criarPainelUsado());
        painelPrincipal.add(criarPainelFinanciamento());
        painelPrincipal.add(criarPainelBotoes());
        painelPrincipal.add(criarPainelResultado());

        JPanel conteudo = new JPanel(new BorderLayout(10, 10));
        conteudo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        conteudo.add(criarCabecalho(), BorderLayout.NORTH);
        conteudo.add(painelPrincipal, BorderLayout.CENTER);
        conteudo.add(criarPainelMarca(), BorderLayout.EAST);

        add(conteudo);

        //no painel inicial os paineis ficam escondidos
        painelUsado.setVisible(false);
        lblEntrada.setVisible(false);
        txtEntrada.setVisible(false);
        painelResultado.setVisible(false);

        aplicarTema(TemaMarca.porNome((String) cbMarca.getSelectedItem()));

        pack();
        setLocationRelativeTo(null);
    }

    //faixa colorida com o nome do simulador
    JLabel criarCabecalho() {
        lblCabecalho = new JLabel("SIMULADOR DE FINANCIAMENTO", SwingConstants.CENTER);
        lblCabecalho.setOpaque(true);
        lblCabecalho.setForeground(Color.WHITE);
        lblCabecalho.setFont(lblCabecalho.getFont().deriveFont(Font.BOLD, 20f));
        lblCabecalho.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return lblCabecalho;
    }

    //vitrine com a foto e o apelido da marca escolhida
    JPanel criarPainelMarca() {
        painelMarca = new JPanel(new BorderLayout(5, 5));
        painelMarca.putClientProperty("titulo", "A Joia da Marca");
        painelsTematicos.add(painelMarca);

        lblFoto = new JLabel("", SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(LARGURA_FOTO, ALTURA_FOTO));

        lblApelido = new JLabel("", SwingConstants.CENTER);
        lblApelido.setFont(lblApelido.getFont().deriveFont(Font.ITALIC, 12f));
        lblApelido.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        painelMarca.add(lblFoto, BorderLayout.CENTER);
        painelMarca.add(lblApelido, BorderLayout.SOUTH);

        return painelMarca;
    }

    //painel com os dados básicos do veiculo
    JPanel criarPainelVeiculo() {
        JPanel painel = criarPainelTematico(new GridLayout(4, 2, 5, 5), "Dados do Veiculo");

        cbMarca = new JComboBox<>(TemaMarca.nomes());
        cbMarca.addActionListener(e ->
                aplicarTema(TemaMarca.porNome((String) cbMarca.getSelectedItem())));

        txtModelo = new JTextField();

        //ano de 2026 ate 2000
        Integer[] anos = new Integer[27];
        int i = 0;
        for (int ano = 2026; ano >= 2000; ano--) {
            anos[i] = ano;
            i++;
        }
        cbAno = new JComboBox<>(anos);

        txtValor = new JTextField();
        aplicarMascaMoeda(txtValor);

        painel.add(new JLabel("Marca:"));
        painel.add(cbMarca);
        painel.add(new JLabel("Modelo:"));
        painel.add(txtModelo);
        painel.add(new JLabel("Ano:"));
        painel.add(cbAno);
        painel.add(new JLabel("Valor do carro:"));
        painel.add(txtValor);

        return painel;
    }

    //painel com botões de novo ou usado
    JPanel criarPainelTipo() {
        JPanel painel = criarPainelTematico(new FlowLayout(FlowLayout.LEFT), "Tipo do Veiculo");

        rbNovo = new JRadioButton("Novo", true);
        rbUsado = new JRadioButton("Usado");
        rbNovo.setOpaque(false);
        rbUsado.setOpaque(false);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbNovo);
        grupo.add(rbUsado);

        rbNovo.addActionListener(e -> {
            painelUsado.setVisible(false);
            pack();
        });

        rbUsado.addActionListener(e -> {
            painelUsado.setVisible(true);
            pack();
        });

        painel.add(rbNovo);
        painel.add(rbUsado);

        return painel;
    }

    //painel do veículo usado
    JPanel criarPainelUsado() {
        painelUsado = criarPainelTematico(new GridLayout(2, 2, 5, 5), "Veiculo Usado");

        txtKm = new JTextField();
        aplicarMascaMoeda(txtKm);
        txtDonos = new JTextField();

        painelUsado.add(new JLabel("Quilometragem:"));
        painelUsado.add(txtKm);
        painelUsado.add(new JLabel("Numero de proprietarios:"));
        painelUsado.add(txtDonos);

        return painelUsado;
    }

    //painel de financiamento
    JPanel criarPainelFinanciamento() {
        JPanel painel = criarPainelTematico(new GridLayout(3, 2, 5, 5), "Financiamento");

        chkEntrada = new JCheckBox("Possui entrada");
        chkEntrada.setOpaque(false);
        lblEntrada = new JLabel("Valor de entrada:");
        txtEntrada = new JTextField();
        aplicarMascaMoeda(txtEntrada);

        chkEntrada.addActionListener(e -> {
            boolean marcado = chkEntrada.isSelected();
            lblEntrada.setVisible(marcado);
            txtEntrada.setVisible(marcado);
            pack();
        });

        String[] parcelas = {"12", "24", "36", "48", "60"};
        cbParcelas = new JComboBox<>(parcelas);

        painel.add(chkEntrada);
        painel.add(new JLabel(""));
        painel.add(lblEntrada);
        painel.add(txtEntrada);
        painel.add(new JLabel("Parcelas:"));
        painel.add(cbParcelas);

        return painel;
    }

    //painel dos botões
    JPanel criarPainelBotoes() {
        painelBotoes = new JPanel(new FlowLayout());

        btnLimpar = new JButton("Limpar");
        btnCalcular = new JButton("Calcular");
        btnCalcular.setFont(btnCalcular.getFont().deriveFont(Font.BOLD));
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setOpaque(true);
        btnCalcular.setBorderPainted(false);

        btnLimpar.addActionListener(e -> limpar());
        btnCalcular.addActionListener(e -> calcular());

        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnCalcular);

        return painelBotoes;
    }

    //painel do resultado
    JPanel criarPainelResultado() {
        painelResultado = criarPainelTematico(new GridLayout(3, 2, 5, 5), "Resultado");

        lblFinanciado = new JLabel("-");
        lblParcela = new JLabel("-");
        lblTotal = new JLabel("-");
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD));

        painelResultado.add(new JLabel("Valor financiado:"));
        painelResultado.add(lblFinanciado);
        painelResultado.add(new JLabel("Valor da parcela:"));
        painelResultado.add(lblParcela);
        painelResultado.add(new JLabel("Total a pagar:"));
        painelResultado.add(lblTotal);

        return painelResultado;
    }

    //painel com borda titulada que muda de cor junto com a marca
    JPanel criarPainelTematico(LayoutManager layout, String titulo) {
        JPanel painel = new JPanel(layout);
        painel.putClientProperty("titulo", titulo);
        painelsTematicos.add(painel);
        return painel;
    }

    //pinta a tela inteira com a cara da marca escolhida
    void aplicarTema(TemaMarca tema) {
        lblCabecalho.setBackground(tema.cor);
        lblCabecalho.setText("SIMULADOR DE FINANCIAMENTO - " + tema.nome.toUpperCase());

        for (JPanel painel : painelsTematicos) {
            painel.setBackground(tema.corClara());

            String titulo = (String) painel.getClientProperty("titulo");
            TitledBorder borda = BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(tema.cor, 2), titulo);
            borda.setTitleColor(tema.cor.darker());
            borda.setTitleFont(borda.getTitleFont().deriveFont(Font.BOLD));
            painel.setBorder(borda);
        }

        painelPrincipal.setBackground(tema.corClara());
        painelBotoes.setBackground(tema.corClara());
        btnCalcular.setBackground(tema.cor);
        btnLimpar.setBackground(tema.corMedia());

        lblTotal.setForeground(tema.cor.darker());
        lblApelido.setForeground(tema.cor.darker());
        lblApelido.setText("<html><center>" + tema.apelido + "</center></html>");

        ImageIcon foto = tema.getFoto(LARGURA_FOTO, ALTURA_FOTO);
        lblFoto.setIcon(foto);
        lblFoto.setText(foto == null ? "(foto nao encontrada)" : "");

        pack();
    }

    //deixa o campo formatado sozinho enquanto a pessoa digita
    void aplicarMascaMoeda(JTextField campo) {
        campo.getDocument().addDocumentListener(new DocumentListener() {

            // trava para o próprio campo.setText() não dispara o formatar() de novo
            boolean atualizando = false;

            public void insertUpdate(DocumentEvent e) {
                formatar();
            }

            public void removeUpdate(DocumentEvent e) {
                formatar();
            }

            public void changedUpdate(DocumentEvent e) {
            }

            void formatar() {
                if (atualizando) {
                    return;
                }
                atualizando = true;

                //pega só os digitos que a pessoa digitou
                String digitos = campo.getText().replaceAll("[^0-9]", "");

                // atualiza o texto do campo depois, pra nao dar erro mexendo no Document durante o evento
                SwingUtilities.invokeLater(() -> {
                    if (digitos.isEmpty()) {
                        campo.setText("");
                    } else {
                        // trata os 2 ultimos digitos como centavos
                        double valor = Long.parseLong(digitos) / 100.0;
                        campo.setText(formatoNumero.format(valor));
                        campo.setCaretPosition(campo.getText().length());
                    }
                    atualizando = false;
                });
            }
        });
    }

    //converte o texto formatado
    double converterParaDouble(JTextField campo) {
        String texto = campo.getText().trim().replace(".", "").replace(",", ".");
        return Double.parseDouble(texto);
    }

    //limpa todos os campos do formulário
    void limpar() {
        cbMarca.setSelectedIndex(0);
        txtModelo.setText("");
        cbAno.setSelectedIndex(0);
        txtValor.setText("");

        rbNovo.setSelected(true);
        painelUsado.setVisible(false);
        txtKm.setText("");
        txtDonos.setText("");

        chkEntrada.setSelected(false);
        lblEntrada.setVisible(false);
        txtEntrada.setVisible(false);
        txtEntrada.setText("");
        cbParcelas.setSelectedIndex(0);

        painelResultado.setVisible(false);
        lblFinanciado.setText("-");
        lblParcela.setText("-");
        lblTotal.setText("-");

        pack();
    }

    //valida os dados e calcula o financiamento
    void calcular() {
        // valida o modelo
        if (txtModelo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o modelo do veiculo.");
            return;
        }

        //valida o valor do veículo
        double valorVeiculo;
        try {
            valorVeiculo = converterParaDouble(txtValor);
            if (valorVeiculo <= 0) {
                JOptionPane.showMessageDialog(this, "O valor do carro deve ser maior que zero.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor do carro invalido.");
            return;
        }

        //valida os dados do veículo usado
        if (rbUsado.isSelected()) {
            if (txtKm.getText().trim().isEmpty() || txtDonos.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha a quilometragem e o numero de proprietarios.");
                return;
            }
            try {
                converterParaDouble(txtKm);
                Integer.parseInt(txtDonos.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quilometragem ou numero de proprietarios invalido.");
                return;
            }
        }

        //valida a entrada
        double entrada = 0;
        if (chkEntrada.isSelected()) {
            try {
                entrada = converterParaDouble(txtEntrada);
                if (entrada < 0 || entrada >= valorVeiculo) {
                    JOptionPane.showMessageDialog(this, "O valor de entrada deve ser maior ou igual a zero e menor que o valor do carro.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor de entrada invalido.");
                return;
            }
        }

        int parcelas = Integer.parseInt((String) cbParcelas.getSelectedItem());

        //aqui a tela so chama a classe Financiamento, não faz a conta sozinha
        Financiamento financiamento = new Financiamento(valorVeiculo, entrada, parcelas);

        lblFinanciado.setText(formatoMoeda.format(financiamento.getValorFinanciado()));
        lblParcela.setText(parcelas + "x de " + formatoMoeda.format(financiamento.getValorParcela()));
        lblTotal.setText(formatoMoeda.format(financiamento.getTotalPagar()));

        painelResultado.setVisible(true);
        pack();
    }
}
