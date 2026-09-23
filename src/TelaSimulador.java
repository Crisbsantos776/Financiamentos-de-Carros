import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.NumberFormat;
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

    //resultado
    JPanel painelResultado;
    JLabel lblFinanciado;
    JLabel lblParcela;
    JLabel lblTotal;

    JPanel painelPrincipal;

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

        add(painelPrincipal);

        //no painel inicial os paineis ficam escondidos
        painelUsado.setVisible(false);
        lblEntrada.setVisible(false);
        txtEntrada.setVisible(false);
        painelResultado.setVisible(false);

        pack();
        setLocationRelativeTo(null);
    }

    //painel com os dados básicos do veiculo
    JPanel criarPainelVeiculo() {
        JPanel painel = new JPanel(new GridLayout(4, 2, 5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Dados do Veiculo"));

        String[] marcas = {"Chevrolet", "Fiat", "Ford", "Honda", "Hyundai",
                "Renault", "Toyota", "Volkswagen"};
        cbMarca = new JComboBox<>(marcas);

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
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painel.setBorder(BorderFactory.createTitledBorder("Tipo do Veiculo"));

        rbNovo = new JRadioButton("Novo", true);
        rbUsado = new JRadioButton("Usado");

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
        painelUsado = new JPanel(new GridLayout(2, 2, 5, 5));
        painelUsado.setBorder(BorderFactory.createTitledBorder("Veiculo Usado"));

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
        JPanel painel = new JPanel(new GridLayout(3, 2, 5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Financiamento"));

        chkEntrada = new JCheckBox("Possui entrada");
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
        JPanel painel = new JPanel(new FlowLayout());

        btnLimpar = new JButton("Limpar");
        btnCalcular = new JButton("Calcular");

        btnLimpar.addActionListener(e -> limpar());
        btnCalcular.addActionListener(e -> calcular());

        painel.add(btnLimpar);
        painel.add(btnCalcular);

        return painel;
    }

    //painel do resultado
    JPanel criarPainelResultado() {
        painelResultado = new JPanel(new GridLayout(3, 2, 5, 5));
        painelResultado.setBorder(BorderFactory.createTitledBorder("Resultado"));

        lblFinanciado = new JLabel("-");
        lblParcela = new JLabel("-");
        lblTotal = new JLabel("-");

        painelResultado.add(new JLabel("Valor financiado:"));
        painelResultado.add(lblFinanciado);
        painelResultado.add(new JLabel("Valor da parcela:"));
        painelResultado.add(lblParcela);
        painelResultado.add(new JLabel("Total a pagar:"));
        painelResultado.add(lblTotal);

        return painelResultado;
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