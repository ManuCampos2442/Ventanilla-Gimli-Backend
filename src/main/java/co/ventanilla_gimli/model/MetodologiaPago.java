package co.ventanilla_gimli.model;

public enum MetodologiaPago {

    TARJETA_DE_CREDITO("Tarjeta de Credito"),
    TARJETA_DE_DEBITO("Tarjeta de Debito"),
    NEQUI("Nequi"),
    DAVIPLATA("Daviplata");

    private String nombre;
    MetodologiaPago(String nombre){
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }
}
