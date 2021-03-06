package contextoProblema;

import datos.ControlCSV;
import datos.Datos;

import javax.naming.ldap.Control;
import java.util.ArrayList;


public class Tienda {

	private Mesa[] mesas = {new Mesa(this),new Mesa(this),new Mesa(this),new Mesa(this)};

	private Caja caja;
	private Cocina cocina;
	private Inventario inventario = new Inventario();

	public Inventario getInventario() {
		return inventario;
	}


	public Tienda() {
		caja = new Caja();
		cocina = new Cocina(this);
		//inventario.comprarAutomatico();
	}

	public Caja getCaja() {
		return caja;
	}

	public Cocina getCocina() {
		return cocina;
	}



	public void ocuparMesa(int nroMesa){
		if(!mesas[nroMesa].esOcupado()) {
			int nroBoletas=buscarUltimaBoleta();
			System.out.println("Se crea una boleta con id = "+(nroBoletas));
			Boleta boleta = new Boleta(nroBoletas);
			mesas[nroMesa] = new Mesa(this);
			mesas[nroMesa].setBoleta(boleta);
			mesas[nroMesa].setOcupado(true);
		}
	}
	private int buscarUltimaBoleta(){
		Datos datos = new Datos();
		int id=datos.nroBoletas();
		for (Mesa m:mesas){
			if(m.esOcupado()){
				if(m.getBoleta().getNroID()>=id){
					id=m.getBoleta().getNroID()+1;
				}
			}
		}
		return id;
	}


	public void comprarAutomatico(){
		double gasto = calcularGastoAutomatico();
		for(Ingredientes i:Ingredientes.values()) {
			i.setCantidad(i.getMax());
		}
		caja.hacer_Egreso(gasto);
	}

	private double calcularGastoAutomatico() {
		double total=0;
		double gasto;
		for(Ingredientes i:Ingredientes.values()){
			gasto=(i.getPrecio_compra_min()*(i.getMax()-i.getCantidad()))/i.getMinimo();
			total+=gasto;
		}
		return total;
	}


	public void comprarPersonalizado(double[] pedido) {
		for(Ingredientes i:Ingredientes.values()){
			i.setCantidad(i.getCantidad()+pedido[i.ordinal()]);
		}
		double gasto = calcularGastoPersonalizado(pedido);
		caja.hacer_Egreso(gasto);
	}

	private double calcularGastoPersonalizado(double[] pedido) {
		double total=0;
		double gasto;
		for(Ingredientes i:Ingredientes.values()){
			gasto=(i.getPrecio_compra_min()*pedido[i.ordinal()])/i.getMinimo();
			total+=gasto;
		}
		return total;
	}



	public Mesa getMesa(int nroMesa) {
		return mesas[nroMesa];
	}

    public void guardarDatos() {
		Datos datos = new Datos();
		datos.guardarInventario(inventario);
		datos.guardarBalance();
		//System.out.println("Datos Guardados");
    }
    public void traerDatos(){
		Datos datos = new Datos();
		inventario.actualizarCon(datos.obtenerInventario());
	}
}