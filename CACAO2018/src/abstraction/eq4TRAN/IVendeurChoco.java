package abstraction.eq4TRAN;

import java.util.ArrayList;

import abstraction.eq4TRAN.VendeurChoco.GPrix;
import 

abstraction.eq4TRAN.VendeurChoco.GQte;

/**
 * 
 * @author Etienne
 *
 */
public interface IVendeurChoco {

	public GQte getStock();
	
	public GPrix getPrix();
@Deprecated	
	public GQte getLivraison(GQte[] commandes);
	
}
