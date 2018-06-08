package abstraction.eq4TRAN;

import java.util.ArrayList;

import abstraction.eq3PROD.echangesProdTransfo.ContratFeve;
import abstraction.eq3PROD.echangesProdTransfo.IAcheteurFeve;
import abstraction.eq4TRAN.ITransformateur;
import abstraction.eq4TRAN.VendeurChoco.GPrix;
import abstraction.eq4TRAN.VendeurChoco.GQte;
import abstraction.eq4TRAN.VendeurChoco.Vendeur;
import abstraction.eq7TRAN.echangeTRANTRAN.ContratPoudre;
import abstraction.eq7TRAN.echangeTRANTRAN.IAcheteurPoudre;
import abstraction.eq7TRAN.echangeTRANTRAN.IVendeurPoudre;
import abstraction.fourni.Acteur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

/**
 * 
 * @author Noémie Rigaut
 *
 */

public class Eq4TRAN implements Acteur, 
ITransformateur, 
IAcheteurFeve,
IVendeurChoco,
IAcheteurPoudre,
IVendeurPoudre{ 

	public Acteur Eq4TRAN ; 

	/** Déclaration des indicateurs pour le Journal
	 *  
	 */
	private Indicateur stockTabBQ ;
	private Indicateur stockTabMQ ;
	private Indicateur stockTabHQ ;
	private Indicateur stockChocMQ ;
	private Indicateur stockChocHQ ;
	private Indicateur prodTabBQ ;
	private Indicateur prodTabMQ ;
	private Indicateur prodTabHQ ;
	private Indicateur prodChocMQ ;
	private Indicateur prodChocHQ ;
	private Indicateur solde ; 
	private Journal JournalEq4 = new Journal("JournalEq4") ;
	private Vendeur vendeur;

	/** Contrats en cours pour la méthode next interne
	 * 
	 */
	private ContratFeve[] contratFeveEnCours ; 
	private ContratPoudre[] contratPoudreEnCoursEq7TRAN ;
	private ContratPoudre[] contratPoudreEnCoursEq5TRAN;
	/** Initialisation des indicateurs 
	 * 
	 */
	public Eq4TRAN() {
		
		/**@Mickaël
		 */
		contratPoudreEnCoursEq7TRAN = new ContratPoudre[3];
		contratPoudreEnCoursEq5TRAN = new ContratPoudre[3];
		contratPoudreEnCoursEq5TRAN[0] = null;
		contratPoudreEnCoursEq5TRAN[1] = new ContratPoudre(1,27000,100.0, (IAcheteurPoudre)this, (IVendeurPoudre)Monde.LE_MONDE.getActeur("Eq5TRAN"),false);
		contratPoudreEnCoursEq5TRAN[2] = null;
		contratPoudreEnCoursEq7TRAN[0] = null;
		contratPoudreEnCoursEq7TRAN[2] = new ContratPoudre(2,18000,100.0, (IAcheteurPoudre)this,(IVendeurPoudre) Monde.LE_MONDE.getActeur("Eq7TRAN"),false);
		contratPoudreEnCoursEq7TRAN[1] = null;

		stockTabBQ = new Indicateur("stockTabBQ",this,1000) ;
		stockTabMQ = new Indicateur("stockTabMQ",this,1000) ;
		stockTabHQ = new Indicateur("stockTabHQ",this,1000) ;
		stockChocMQ = new Indicateur("stockChocMQ",this,1000) ;
		stockChocHQ = new Indicateur("stockTabHQ",this,1000) ;
		prodTabBQ = new Indicateur("prodTabBQ",this,1000) ;
		prodTabMQ = new Indicateur("prodTabMQ",this,1000) ;
		prodTabHQ = new Indicateur("prodTabHQ",this,1000) ;
		prodChocMQ = new Indicateur("prodChocMQ",this,1000) ;
		prodChocHQ = new Indicateur("prodChocHQ",this,1000) ;
		solde = new Indicateur("solde",this,1000) ;
		vendeur = new Vendeur(0.0, stockChocMQ.getValeur(), stockChocHQ.getValeur(), stockTabBQ.getValeur(), stockTabMQ.getValeur(), stockTabHQ.getValeur());
	}

	/** Nom de l'acteur
	 */
	@Override
	public String getNom() {
		return "Eq4TRAN";
	}


	public void next() {
		/**
		 *  On récupère les contrats màj par la méthode next du marché
		 *  ?????????????????????????
		 */

		/**
		 * pour chaque contrat on récupère prix et qté
		 */
		/**
		 * pour contrat fève 
		 */
		for(int i = 0 ; i < contratFeveEnCours.length ; i++) {
			/**
			 * Selon la qualité
			 * On récupère les qtés de fèves achetées
			 * Elles sont transformées immédiatement en produits
			 * Les produits sont ajoutés aux stocks
			 * Le coût total de l'achat est retiré au solde
			 */
			if (contratFeveEnCours[i].getReponse()) {
				if(contratFeveEnCours[i].getQualite() == 0) {
					prodTabBQ.setValeur(Eq4TRAN, contratFeveEnCours[i].getQuantite()); 
					double ancienStockTabBQ = stockTabBQ.getValeur() ;
					stockTabBQ.setValeur(Eq4TRAN, ancienStockTabBQ + prodTabBQ.getValeur());
					solde.setValeur(Eq4TRAN, contratFeveEnCours[i].getPrix()*contratFeveEnCours[i].getQuantite());
				}
				else if(contratFeveEnCours[i].getQualite() == 1) {
					prodTabMQ.setValeur(Eq4TRAN, contratFeveEnCours[i].getQuantite());
					double ancienStockTabMQ = stockTabMQ.getValeur() ;
					stockTabMQ.setValeur(Eq4TRAN, ancienStockTabMQ + prodTabMQ.getValeur());
					solde.setValeur(Eq4TRAN, contratFeveEnCours[i].getPrix()*contratFeveEnCours[i].getQuantite());

				}
				else if(contratFeveEnCours[i].getQualite() == 2) {
					prodTabHQ.setValeur(Eq4TRAN, contratFeveEnCours[i].getQuantite());
					double ancienStockTabHQ = stockTabHQ.getValeur() ;
					stockTabHQ.setValeur(Eq4TRAN, ancienStockTabHQ + prodTabMQ.getValeur());
					solde.setValeur(Eq4TRAN, contratFeveEnCours[i].getPrix()*contratFeveEnCours[i].getQuantite());

				}
			}

		}
		
		
		ArrayList<ContratPoudre> contratPoudreEnCours = null;
		contratPoudreEnCours.add(contratPoudreEnCoursEq5TRAN[0]);
		contratPoudreEnCours.add(contratPoudreEnCoursEq5TRAN[1]);
		contratPoudreEnCours.add(contratPoudreEnCoursEq5TRAN[2]);
		contratPoudreEnCours.add(contratPoudreEnCoursEq7TRAN[0]);
		contratPoudreEnCours.add(contratPoudreEnCoursEq7TRAN[1]);
		contratPoudreEnCours.add(contratPoudreEnCoursEq7TRAN[2]);
		
		for(int i = 0 ; i < contratPoudreEnCours.size() ; i++ ) {

			/**
			 * On récupère les qtés de poudre achetée
			 * On les transforme en produits
			 * Puis on les stocke
			 */

			if(contratPoudreEnCours.get(i).getReponse()) {
				if (contratPoudreEnCours.get(i).getQualite() == 1) {
					prodChocMQ.setValeur(Eq4TRAN, contratPoudreEnCours.get(i).getQuantite());
					double ancienStockChocMQ = stockChocMQ.getValeur() ;
					stockChocMQ.setValeur(Eq4TRAN, ancienStockChocMQ + prodChocMQ.getValeur());
					solde.setValeur(Eq4TRAN, contratPoudreEnCours.get(i).getPrix()*contratPoudreEnCours.get(i).getQuantite());

				} else if (contratPoudreEnCours.get(i).getQualite() == 2 ) {
					prodChocHQ.setValeur(Eq4TRAN, contratFeveEnCours[i].getQuantite());
					double ancienStockChocHQ = stockChocHQ.getValeur() ;
					stockChocHQ.setValeur(Eq4TRAN, ancienStockChocHQ + prodChocHQ.getValeur()); 
					solde.setValeur(Eq4TRAN, contratPoudreEnCours.get(i).getPrix()*contratPoudreEnCours.get(i).getQuantite());
				}
			}
		} 


		/** Màj des stocks pour les distributeurs
		 * 
		 */

	}

	public void journalEq4() {
		JournalEq4.ajouter("Stock des tablettes Basse Qualité = "+stockTabBQ.getValeur());
		JournalEq4.ajouter("Stock des tablettes Moyenne Qualité = "+stockTabMQ.getValeur());
		JournalEq4.ajouter("Stock des tablettes Basse Qualité = "+stockTabHQ.getValeur());
		JournalEq4.ajouter("Stock des chocolats Moyenne Qualité = "+stockChocMQ.getValeur());
		JournalEq4.ajouter("Stock des chocolats Haute Qualité = "+stockChocHQ.getValeur());
		JournalEq4.ajouter("Production des tablettes Basse Qualité = "+prodTabBQ.getValeur());
		JournalEq4.ajouter("Production des tablettes Moyenne Qualité = "+prodTabBQ.getValeur());
		JournalEq4.ajouter("Production des tablettes Haute Qualité = "+prodTabBQ.getValeur());
		JournalEq4.ajouter("Production de chocolats Moyenne Qualité = "+prodChocMQ.getValeur());
		JournalEq4.ajouter("Production de chocolats Haute Qualité = "+prodChocHQ.getValeur());

	}


	@Override
	public void sell(int q) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendOffrePublique(ContratFeve[] offrePublique) {
		this.contratFeveEnCours=offrePublique;
	}


	@Override
	public ContratFeve[] getDemandePrivee() {

		return null ;
	}


	@Override
	public void sendContratFictif() {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendOffreFinale(ContratFeve[] offreFinale) {
		// TODO Auto-generated method stub

	}


	@Override
	public ContratFeve[] getResultVentes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GQte getStock() {

		return vendeur.getStock();
	}

	@Override
	public GPrix getPrix() {
		// TODO Auto-generated method stub
		return vendeur.getPrix();
	}

	@Override
	public ArrayList<GQte> getLivraison(ArrayList<GQte> commandes) {
		// TODO Auto-generated method stub
		return vendeur.getLivraison(commandes);
	}

	@Override
	public ContratPoudre[] getCataloguePoudre(IAcheteurPoudre acheteur) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContratPoudre[] getDevisPoudre(ContratPoudre[] demande, IAcheteurPoudre acheteur) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendReponsePoudre(ContratPoudre[] devis, IAcheteurPoudre acheteur) {
		// TODO Auto-generated method stub

	}

	@Override
	public ContratPoudre[] getEchangeFinalPoudre(ContratPoudre[] contrat, IAcheteurPoudre acheteur) {
		// TODO Auto-generated method stub
		return null;
	}

}
