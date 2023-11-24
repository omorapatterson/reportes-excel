package catalogo.reportes.core.services.interfaces;

public interface ISincronizadorService {

    public boolean enviarListaDeVentaQueSeEstaActualizando(String listaDeVentaGln);

    boolean enviarProductoQueFueActualizado(String idProducto);

    boolean enviarNotificacionAlActualizarGrupos(String porciento);

    public String login();

}
