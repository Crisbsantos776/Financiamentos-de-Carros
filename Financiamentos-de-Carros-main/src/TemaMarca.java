import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.net.URL;

//cada marca tem sua cor, sua foto e seu apelido carinhoso
public class TemaMarca {

    final String nome;
    final String apelido;
    final Color cor;
    final String arquivo;

    private ImageIcon fotoCache;

    private TemaMarca(String nome, String apelido, Color cor, String arquivo) {
        this.nome = nome;
        this.apelido = apelido;
        this.cor = cor;
        this.arquivo = arquivo;
    }

    static final TemaMarca[] TODAS = {
            new TemaMarca("Chevrolet", "Dragao de lata - assusta ate o gerente do banco",
                    new Color(0x2F6F8F), "chevrolet.jpg"),
            new TemaMarca("Fiat", "Versao hibrida de verdade: gasolina e aveia",
                    new Color(0xC0392B), "fiat.jpg"),
            new TemaMarca("Ford", "Abre a boca e engole a sua entrada",
                    new Color(0xD35400), "ford.jpg"),
            new TemaMarca("Honda", "Coelhinho felpudo, so nao molha",
                    new Color(0x8D6E63), "honda.jpg"),
            new TemaMarca("Hyundai", "Feito 100% de material reciclado (e de bugiganga)",
                    new Color(0x1F6FB2), "hyundai.jpg"),
            new TemaMarca("Renault", "Salto 15, automatico, ideal pra bater ponto",
                    new Color(0xB3123F), "renault.jpg"),
            new TemaMarca("Toyota", "Consumo baixo, colesterol alto",
                    new Color(0xA0522D), "toyota.jpg"),
            new TemaMarca("Volkswagen", "Cabe na vaga de moto e na sua prestacao",
                    new Color(0x4A5A6A), "volkswagen.jpg")
    };

    static String[] nomes() {
        String[] nomes = new String[TODAS.length];
        for (int i = 0; i < TODAS.length; i++) {
            nomes[i] = TODAS[i].nome;
        }
        return nomes;
    }

    static TemaMarca porNome(String nome) {
        for (TemaMarca tema : TODAS) {
            if (tema.nome.equals(nome)) {
                return tema;
            }
        }
        return TODAS[0];
    }

    //versao mais clara da cor, pra usar como fundo sem cansar a vista
    Color corClara() {
        return misturarComBranco(0.88);
    }

    Color corMedia() {
        return misturarComBranco(0.70);
    }

    private Color misturarComBranco(double peso) {
        int r = (int) (cor.getRed() + (255 - cor.getRed()) * peso);
        int g = (int) (cor.getGreen() + (255 - cor.getGreen()) * peso);
        int b = (int) (cor.getBlue() + (255 - cor.getBlue()) * peso);
        return new Color(r, g, b);
    }

    //carrega a foto uma vez so e ja redimensiona pro tamanho do painel
    ImageIcon getFoto(int largura, int altura) {
        if (fotoCache == null) {
            ImageIcon original = carregarOriginal();
            if (original == null) {
                return null;
            }
            fotoCache = escalarMantendoProporcao(original, largura, altura);
        }
        return fotoCache;
    }

    private ImageIcon carregarOriginal() {
        // roda tanto com as imagens no classpath quanto direto da pasta do projeto
        URL doClasspath = TemaMarca.class.getResource("/marcas/" + arquivo);
        if (doClasspath != null) {
            return new ImageIcon(doClasspath);
        }

        String[] caminhos = {
                "resources/marcas/" + arquivo,
                "../resources/marcas/" + arquivo,
                "src/../resources/marcas/" + arquivo
        };
        for (String caminho : caminhos) {
            File arquivoLocal = new File(caminho);
            if (arquivoLocal.isFile()) {
                return new ImageIcon(arquivoLocal.getAbsolutePath());
            }
        }
        return null;
    }

    private static ImageIcon escalarMantendoProporcao(ImageIcon original, int largura, int altura) {
        double escala = Math.min(
                largura / (double) original.getIconWidth(),
                altura / (double) original.getIconHeight());
        int novaLargura = (int) (original.getIconWidth() * escala);
        int novaAltura = (int) (original.getIconHeight() * escala);
        Image imagem = original.getImage().getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);
        return new ImageIcon(imagem);
    }
}
