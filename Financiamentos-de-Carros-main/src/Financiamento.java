public class Financiamento {

    static final double TAXA = 0.15;

    double valorVeiculo;
    double entrada;
    int parcelas;

    public Financiamento(double valorVeiculo, double entrada, int parcelas) {
        this.valorVeiculo = valorVeiculo;
        this.entrada = entrada;
        this.parcelas = parcelas;
    }

    public double getValorFinanciado() {
        return valorVeiculo - entrada;
    }

    public double getValorParcela() {
        double valorTotal = getValorFinanciado() * (1 + TAXA);
        return valorTotal / parcelas;
    }

    public double getTotalPagar() {
        return getValorParcela() * parcelas;
    }
}