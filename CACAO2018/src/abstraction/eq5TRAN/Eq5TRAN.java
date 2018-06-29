package abstraction.eq5TRAN; 	  				 	 	   			 	
 	  				 	 	   			 	
import abstraction.eq3PROD.echangesProdTransfo.ContratFeve;
import abstraction.eq3PROD.echangesProdTransfo.ContratFeveV3;  	  				 	 	   			 	

import abstraction.eq3PROD.echangesProdTransfo.IAcheteurFeve;
import abstraction.eq3PROD.echangesProdTransfo.IAcheteurFeveV2;
import abstraction.eq3PROD.echangesProdTransfo.IAcheteurFeveV4;
import abstraction.eq3PROD.echangesProdTransfo.IVendeurFeve; 	  				 	 	   			 	
import abstraction.eq3PROD.echangesProdTransfo.IVendeurFeveV2;
import abstraction.eq3PROD.echangesProdTransfo.IVendeurFeveV4; 	  				 	 	   			 	

import abstraction.eq5TRAN.appeldOffre.DemandeAO; 	  				 	 	   			 	
import abstraction.eq5TRAN.appeldOffre.IvendeurOccasionnelChoco;
import abstraction.eq5TRAN.appeldOffre.IvendeurOccasionnelChocoBis;
import abstraction.eq5TRAN.util.Marchandises; 	  				 	 	   			 	
import abstraction.eq7TRAN.echangeTRANTRAN.ContratPoudre; 	  				 	 	   			 	
import abstraction.eq7TRAN.echangeTRANTRAN.IAcheteurPoudre; 	  				 	 	   			 	
import abstraction.eq7TRAN.echangeTRANTRAN.IVendeurPoudre; 	  				 	 	   			 	
import abstraction.fourni.Acteur; 	  				 	 	   			 	
import abstraction.fourni.Indicateur; 	  				 	 	   			 	
import abstraction.fourni.Journal; 	  				 	 	   			 	
import abstraction.fourni.Monde; 	  				 	 	   			 	
 	  				 	 	   			 	
import java.util.ArrayList; 	  				 	 	   			 	
import java.util.List; 	  				 	 	   			 	
 	  				 	 	   			 	
import static abstraction.eq5TRAN.util.Marchandises.*; 	  				 	 	   			 	
 	  				 	 	   			 	
/** 	  				 	 	   			 	
 * @author Juliette Gorline (chef) 	  				 	 	   			 	
 * @author Francois Le Guernic 	  				 	 	   			 	
 * @author Maxim Poulsen 	  				 	 	   			 	
 * @author Thomas Schillaci (lieutenant) 	  				 	 	   			 	
 * 	  				 	 	   			 	
 * TODO LIST 	  				 	 	   			 	
 * - Gestion periodes de l'annee (Noel, Pacques ...) 	  				 	 	   			 	
 * - Gestion de facteurs sociaux (greves ...) 	  				 	 	   			 	
 * - Systeme de fidelite client/fournisseur 	  				 	 	   			 	
 */ 	  				 	 	   			 	
public class Eq5TRAN implements Acteur, IAcheteurPoudre, IVendeurPoudre, IvendeurOccasionnelChocoBis,IAcheteurFeveV4 { 	  				 	 	   			 	
 	  				 	 	   			 	
    // cf Marchandises.java pour obtenir l'indexation 	  				 	 	   			 	
    private Indicateur[] productionSouhaitee; // ce qui sort de nos machines en kT 	  				 	 	   			 	
    private Indicateur[] achatsSouhaites; // ce qu'on achète aux producteurs en kT 	  				 	 	   			 	
    private float facteurStock; // facteur lié aux risques= combien d'itérations on peut tenir sans réception de feves/poudre 	  				 	 	   			 	
    private Indicateur[] stocksSouhaites; // margeStock = facteurStock * variationDeStockParIteration, en kT 	  				 	 	   			 	
    private Indicateur[] stocks; // les vrais stocks en kT 	  				 	 	   			 	
    private ContratFeveV3 contratFeveBQEq2; // Le contrat avec l'équipe 2 pour les fèves BQ 	  				 	 	   			 	
    private ContratFeveV3 contratFeveMQEq2; // Le contrat avec l'équipe 2 pour les fèves MQ 	  				 	 	   			 	
    private ContratFeveV3 contratFeveMQEq3; // Le contrat avec l'équipe 3 pour les fèves MQ 	  				 	 	   			 	
 	  				 	 	   			 	
	private Indicateur banque; // en milliers d'euros 	  				 	 	   			 	
	private Indicateur[] prix; // en €/kT 	  				 	 	   			 	
 	  				 	 	   			 	
    private Journal journal; 	  				 	 	   			 	
     	  				 	 	   			 	
    private int[] dureesPeremption; // durees en nombre de next 	  				 	 	   			 	
    /* 	  				 	 	   			 	
     * On regarde pour chaque marchandise sur toute une duree de sa duree de peremption 	  				 	 	   			 	
     * Si on a respecte les objectifs fixes sur chaque next de la periode 	  				 	 	   			 	
     * Sinon on perd 100/(duree du next)% de la marchandise 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    private boolean[] respectObjectifs; 	  				 	 	   			 	
 	  				 	 	   			 	
 	  				 	 	   			 	
    public Eq5TRAN() { 	  				 	 	   			 	
 	  				 	 	   			 	
        /** 	  				 	 	   			 	
         * GESTION DES INDICATEURS 	  				 	 	   			 	
         * @author Thomas Schillaci 	  				 	 	   			 	
         */ 	  				 	 	   			 	
 	  				 	 	   			 	
        int nbMarchandises = Marchandises.getNombreMarchandises(); 	  				 	 	   			 	
        productionSouhaitee = new Indicateur[nbMarchandises]; 	  				 	 	   			 	
        achatsSouhaites = new Indicateur[nbMarchandises]; 	  				 	 	   			 	
        facteurStock = 3; 	  				 	 	   			 	
        stocksSouhaites = new Indicateur[nbMarchandises]; 	  				 	 	   			 	
        stocks = new Indicateur[nbMarchandises]; 	  				 	 	   			 	
        prix = new Indicateur[nbMarchandises]; 	  				 	 	   			 	
 	  				 	 	   			 	
		prix[FEVES_BQ] = new Indicateur("Eq5 - Prix de feves BQ", this, 0); 	  				 	 	   			 	
		prix[FEVES_MQ] = new Indicateur("Eq5 - Prix de feves MQ", this, 0); 	  				 	 	   			 	
		prix[TABLETTES_BQ] = new Indicateur("Eq5 - Prix de tablettes BQ", this, 8000000); 	  				 	 	   			 	
		prix[TABLETTES_MQ] = new Indicateur("Eq5 - Prix de tablettes MQ", this, 10000000); 	  				 	 	   			 	
		prix[TABLETTES_HQ] = new Indicateur("Eq5 - Prix de tablettes HQ", this, 18000000); 	  				 	 	   			 	
		prix[POUDRE_BQ] = new Indicateur("Eq5 - Prix de poudre BQ", this, 0); 	  				 	 	   			 	
		prix[POUDRE_MQ] = new Indicateur("Eq5 - Prix de poudre MQ", this, 8500000); 	  				 	 	   			 	
		prix[POUDRE_HQ] = new Indicateur("Eq5 - Prix de poudre HQ", this, 0); 	  				 	 	   			 	
		prix[FRIANDISES_MQ] = new Indicateur("Eq5 - Prix de friandises MQ", this, 8000000); 	  				 	 	   			 	
 	  				 	 	   			 	
        productionSouhaitee[FEVES_BQ] = new Indicateur("Eq5 - Production souhaitee de feves BQ", this, 0); 	  				 	 	   			 	
        productionSouhaitee[FEVES_MQ] = new Indicateur("Eq5 - Production souhaitee de feves MQ", this, 0); 	  				 	 	   			 	
        productionSouhaitee[TABLETTES_BQ] = new Indicateur("Eq5 - Production souhaitee de tablettes BQ", this, 345); 	  				 	 	   			 	
        productionSouhaitee[TABLETTES_MQ] = new Indicateur("Eq5 - Production souhaitee de tablettes MQ", this, 575); 	  				 	 	   			 	
        productionSouhaitee[TABLETTES_HQ] = new Indicateur("Eq5 - Production souhaitee de tablettes HQ", this, 115); 	  				 	 	   			 	
        productionSouhaitee[POUDRE_BQ] = new Indicateur("Eq5 - Production souhaitee de poudre BQ", this, 360); 	  				 	 	   			 	
        productionSouhaitee[POUDRE_MQ] = new Indicateur("Eq5 - Production souhaitee de poudre MQ", this, 50); 	  				 	 	   			 	
        productionSouhaitee[POUDRE_HQ] = new Indicateur("Eq5 - Production souhaitee de poudre HQ", this, 0); 	  				 	 	   			 	
        productionSouhaitee[FRIANDISES_MQ] = new Indicateur("Eq5 - Production souhaitee de friandises MQ", this, 115); 	  				 	 	   			 	
 	  				 	 	   			 	
        achatsSouhaites[FEVES_BQ] = new Indicateur("Eq5 - Achats souhaites de feves BQ", this, 360); 	  				 	 	   			 	
        achatsSouhaites[FEVES_MQ] = new Indicateur("Eq5 - Achats souhaites de feves MQ", this, 840); 	  				 	 	   			 	
        achatsSouhaites[TABLETTES_BQ] = new Indicateur("Eq5 - Achats souhaites de tablettes BQ", this, 0); 	  				 	 	   			 	
        achatsSouhaites[TABLETTES_MQ] = new Indicateur("Eq5 - Achats souhaites de tablettes MQ", this, 0); 	  				 	 	   			 	
        achatsSouhaites[TABLETTES_HQ] = new Indicateur("Eq5 - Achats souhaites de tablettes HQ", this, 0); 	  				 	 	   			 	
        achatsSouhaites[POUDRE_BQ] = new Indicateur("Eq5 - Achats souhaites de poudre BQ", this, 0); 	  				 	 	   			 	
        achatsSouhaites[POUDRE_MQ] = new Indicateur("Eq5 - Achats souhaites de poudre MQ", this, 0); 	  				 	 	   			 	
        achatsSouhaites[POUDRE_HQ] = new Indicateur("Eq5 - Achats souhaites de poudre HQ", this, 115); 	  				 	 	   			 	
        achatsSouhaites[FRIANDISES_MQ] = new Indicateur("Eq5 - Achats souhaites de friandises MQ", this, 0); 	  				 	 	   			 	
         	  				 	 	   			 	
        for (int i = 0; i < nbMarchandises; i++) { 	  				 	 	   			 	
            stocksSouhaites[i] = new Indicateur("Eq5 - Stocks souhaites de " + Marchandises.getMarchandise(i), this, productionSouhaitee[i].getValeur() + achatsSouhaites[i].getValeur()); 	  				 	 	   			 	
            stocks[i] = new Indicateur("Eq5 - Stocks de " + Marchandises.getMarchandise(i), this, stocksSouhaites[i].getValeur()); // on initialise les vrais stocks comme étant ce que l'on souhaite avoir pour la premiere iteration 	  				 	 	   			 	
        } 	  				 	 	   			 	
 	  				 	 	   			 	
        banque = new Indicateur("Eq5 - Banque", this, 16_000); // environ benefice 2017 sur nombre d'usines 	  				 	 	   			 	
 	  				 	 	   			 	
        Monde.LE_MONDE.ajouterIndicateur(banque); 	  				 	 	   			 	
        Monde.LE_MONDE.ajouterIndicateur(stocks[TABLETTES_BQ]); 	  				 	 	   			 	
        Monde.LE_MONDE.ajouterIndicateur(stocks[TABLETTES_MQ]); 	  				 	 	   			 	
        Monde.LE_MONDE.ajouterIndicateur(stocks[TABLETTES_HQ]); 	  				 	 	   			 	
        Monde.LE_MONDE.ajouterIndicateur(stocks[FRIANDISES_MQ]); 	  				 	 	   			 	
 	  				 	 	   			 	
        /** 	  				 	 	   			 	
         * GESTION DES JOURNAUX 	  				 	 	   			 	
         * @author Thomas Schillaci 	  				 	 	   			 	
         */ 	  				 	 	   			 	
 	  				 	 	   			 	
        journal = new Journal("Journal Eq5"); 	  				 	 	   			 	
        Monde.LE_MONDE.ajouterJournal(journal); 	  				 	 	   			 	
 	  				 	 	   			 	
        /** 	  				 	 	   			 	
         * GESTION DES CONTRATS AVEC LA PRODUCTION 	  				 	 	   			 	
         * @author Francois le Guernic 	  				 	 	   			 	
         */ 	  				 	 	   			 	
 	  				 	 	   			 	
        contratFeveBQEq2 = new ContratFeveV3(this, (IVendeurFeveV4) Monde.LE_MONDE.getActeur("Eq2PROD"), 0);
        contratFeveMQEq2 = new ContratFeveV3(this, (IVendeurFeveV4) Monde.LE_MONDE.getActeur("Eq2PROD"), 1); 	  				 	 	   			 	
        contratFeveMQEq3 = new ContratFeveV3(this, (IVendeurFeveV4) Monde.LE_MONDE.getActeur("Eq3PROD"), 1); 	  				 	 	   			 	
 	  				 	 	   			 	
        /** 	  				 	 	   			 	
         * GESTION DE LA PEREMPTION 	  				 	 	   			 	
         * @author Maxim Poulsen, Thomas Schillaci 	  				 	 	   			 	
         */ 	  				 	 	   			 	
 	  				 	 	   			 	
        dureesPeremption = new int[nbMarchandises]; 	  				 	 	   			 	
        dureesPeremption[FEVES_BQ] = 42; 	  				 	 	   			 	
        dureesPeremption[FEVES_MQ] = (int)(42*0.95f); 	  				 	 	   			 	
        dureesPeremption[TABLETTES_BQ] = 6; 	  				 	 	   			 	
        dureesPeremption[TABLETTES_MQ] = (int)(6*0.95f); 	  				 	 	   			 	
        dureesPeremption[TABLETTES_HQ] = (int)(6*0.90f); 	  				 	 	   			 	
        dureesPeremption[POUDRE_BQ] = 48; 	  				 	 	   			 	
        dureesPeremption[POUDRE_MQ] = (int)(48*0.95f); 	  				 	 	   			 	
        dureesPeremption[POUDRE_HQ] = (int)(48*0.90f); 	  				 	 	   			 	
        dureesPeremption[FRIANDISES_MQ] = (int)(6*0.95f); 	  				 	 	   			 	
 	  				 	 	   			 	
        respectObjectifs = new boolean[nbMarchandises]; 	  				 	 	   			 	
        for (int i = 0; i < Marchandises.getNombreMarchandises(); i++) respectObjectifs[i] = true; 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    @Override 	  				 	 	   			 	
    public String getNom() { 	  				 	 	   			 	
        return "Eq5TRAN"; 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    @Override 	  				 	 	   			 	
    public void next() { 
        production();
        bienEtreSalarie();
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author Thomas Schillaci 	  				 	 	   			 	
     * TODO la prod coute de l'argent 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    public void production() { 	  				 	 	   			 	
        roulement(); 	  				 	 	   			 	
 	  				 	 	   			 	
        production(POUDRE_BQ, TABLETTES_BQ); 	  				 	 	   			 	
        production(POUDRE_MQ, TABLETTES_MQ); 	  				 	 	   			 	
        production(POUDRE_HQ, TABLETTES_HQ); 	  				 	 	   			 	
        production(POUDRE_MQ, FRIANDISES_MQ); 	  				 	 	   			 	
        production(FEVES_BQ, POUDRE_BQ); 	  				 	 	   			 	
        production(FEVES_MQ, POUDRE_MQ); 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author Maxim Poulsen, Thomas Schillaci 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    public void roulement() { 	  				 	 	   			 	
        for (int i = 0; i < Marchandises.getNombreMarchandises(); i++) { 	  				 	 	   			 	
            if(Monde.LE_MONDE.getStep()%dureesPeremption[i]==0 && !respectObjectifs[i]) { 	  				 	 	   			 	
                float perte = 1.0f/dureesPeremption[i]; 	  				 	 	   			 	
                stocks[i].setValeur(this, stocks[i].getValeur() * (1.0f - perte)); 	  				 	 	   			 	
                respectObjectifs[i]=true; // on reinitialise les indicateurs sur les objectifs 	  				 	 	   			 	
                journal.ajouter("L'equipe 5 vient de perdre " + perte*100 + "% de son stock de " + Marchandises.getMarchandise(i) + " à cause d'une mauvaise gestion des durées de péremption"); 	  				 	 	   			 	
            } 	  				 	 	   			 	
        } 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author Thomas Schillaci 	  				 	 	   			 	
     * Transforme la merch1 en merch2 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    public void production(int merch1, int merch2) { 	  				 	 	   			 	
        double quantite = Math.min(stocks[merch1].getValeur(), productionSouhaitee[merch2].getValeur()); 	  				 	 	   			 	
        if(greves()) quantite*=0.3;
        if (quantite < productionSouhaitee[merch2].getValeur())
            journal.ajouter("L'eq. 5 n'a pas pu produire assez de " + Marchandises.getMarchandise(merch2) + " par manque de stock de " + Marchandises.getMarchandise(merch1)); 	  				 	 	   			 	
        stocks[merch1].setValeur(this, stocks[merch1].getValeur() - quantite); 	  				 	 	   			 	
        stocks[merch2].setValeur(this, stocks[merch2].getValeur() + quantite); 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author Thomas Schillaci 	  				 	 	   			 	
     * N.B. accepte les valeurs négatives pour encaisser 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    public void depenser(double depense) { 	  				 	 	   			 	
        double resultat = banque.getValeur() - depense; 	  				 	 	   			 	
        banque.setValeur(this, resultat); 	  				 	 	   			 	
        if (resultat < 0) 	  				 	 	   			 	
            journal.ajouter("L'equipe 5 est a decouvert !\nCompte en banque: " + banque.getValeur() + "€"); 	  				 	 	   			 	
    } 	  		
    
    /*
     * @author Juliette
     */
    public boolean greves() {
    	double probaGreve = 12.0/365; //12 jours de grèves par an
    	if(Math.random()<probaGreve) {
    		journal.ajouter("Les salariés de Nestlé sont en grève");
    		return true;
    	}
    	return false;
    }
    
    /*
     * @author Juliette
     */
    public void bienEtreSalarie() {
    	int numeroNext = Monde.LE_MONDE.getStep();
    	if(numeroNext % 24==0 && this.banque.getValeur()>30000) { // tous les ans on donne 10000€ pour des aménagements pour les salariés
    		this.banque.setValeur(this, this.banque.getValeur()-10000);
    	}
    }
    
    /*
     * @author Juliette
     */
    public void salaires() {
    	depenser((782*2400)/24);
    }
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author Juliette et Thomas 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    @Override 	  				 	 	   			 	
    public ContratPoudre[] getCataloguePoudre(IAcheteurPoudre acheteur) { 	  				 	 	   			 	
        if (stocks[POUDRE_MQ].getValeur() == 0) return new ContratPoudre[0]; 	  				 	 	   			 	
 	  				 	 	   			 	
        ContratPoudre[] catalogue = new ContratPoudre[1]; 	  				 	 	   			 	
        catalogue[0] = new ContratPoudre(1, (int) stocks[POUDRE_MQ].getValeur(), prix[POUDRE_MQ].getValeur(), acheteur, this, false); 	  				 	 	   			 	
        return catalogue; 	  				 	 	   			 	
 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author Juliette 	  				 	 	   			 	
     * V1 : on n'envoie un devis que si la qualité demandée est moyenne (la seule que nous vendons) et que nous avons assez de stocks 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    @Override 	  				 	 	   			 	
    public ContratPoudre[] getDevisPoudre(ContratPoudre[] demande, IAcheteurPoudre acheteur) { 	  				 	 	   			 	
        ContratPoudre[] devis = new ContratPoudre[demande.length]; 	  				 	 	   			 	
        for (int i = 0; i < demande.length; i++) { 	  				 	 	   			 	
            if (demande[i].getQualite() != 1 && demande[i].getQuantite() < stocks[POUDRE_MQ].getValeur()) { 	  				 	 	   			 	
                devis[i] = new ContratPoudre(0, 0, 0, acheteur, this, false); 	  				 	 	   			 	
            } else { 	  				 	 	   			 	
                devis[i] = new ContratPoudre(demande[i].getQualite(), demande[i].getQuantite(), prix[POUDRE_MQ].getValeur(), acheteur, this, false); 	  				 	 	   			 	
            } 	  				 	 	   			 	
        } 	  				 	 	   			 	
 	  				 	 	   			 	
        return devis; 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author Juliette 	  				 	 	   			 	
     * V1 : si la réponse est cohérente avec la demande initiale, nos stocks et nos prix, on répond oui 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    @Override 	  				 	 	   			 	
    public void sendReponsePoudre(ContratPoudre[] devis, IAcheteurPoudre acheteur) { 	  				 	 	   			 	
        ContratPoudre[] reponse = new ContratPoudre[devis.length]; 	  				 	 	   			 	
        for (int i = 0; i < devis.length; i++) { 	  				 	 	   			 	
            if (devis[i].getQualite() != 1 && devis[i].getQuantite() < stocks[POUDRE_MQ].getValeur() && devis[i].getPrix() == prix[POUDRE_MQ].getValeur()) { 	  				 	 	   			 	
                reponse[i] = new ContratPoudre(devis[i].getQualite(), devis[i].getQuantite(), devis[i].getPrix(), devis[i].getAcheteur(), devis[i].getVendeur(), true); 	  				 	 	   			 	
            } else { 	  				 	 	   			 	
                reponse[i] = new ContratPoudre(devis[i].getQualite(), devis[i].getQuantite(), devis[i].getPrix(), devis[i].getAcheteur(), devis[i].getVendeur(), false); 	  				 	 	   			 	
            } 	  				 	 	   			 	
        } 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author Juliette 	  				 	 	   			 	
     * Pour la V1 on suppose que le contrat est entièrement honnoré 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    @Override 	  				 	 	   			 	
    public ContratPoudre[] getEchangeFinalPoudre(ContratPoudre[] contrat, IAcheteurPoudre acheteur) { 	  				 	 	   			 	
        ContratPoudre[] echangesEffectifs = new ContratPoudre[contrat.length]; 	  				 	 	   			 	
        for (int i = 0; i < contrat.length; i++) { 	  				 	 	   			 	
            echangesEffectifs[i] = contrat[i]; 	  				 	 	   			 	
        } 	  				 	 	   			 	
        return echangesEffectifs; 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author Juliette 	  				 	 	   			 	
     * Dans cette méthode, nous sommes ACHETEURS 	  				 	 	   			 	
     * Methode permettant de récupérer les devis de poudre correspondant à nos demandes et de décider si on les accepte ou non 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    private void getTousLesDevisPoudre(ContratPoudre[] demande) { 	  				 	 	   			 	
        List<Acteur> listeActeurs = Monde.LE_MONDE.getActeurs(); 	  				 	 	   			 	
 	  				 	 	   			 	
        List<ContratPoudre[]> devis = new ArrayList<ContratPoudre[]>(); 	  				 	 	   			 	
 	  				 	 	   			 	
        for (Acteur acteur : listeActeurs) { 	  				 	 	   			 	
            if (acteur instanceof IVendeurPoudre) { 	  				 	 	   			 	
                devis.add(((IVendeurPoudre) acteur).getDevisPoudre(demande, this)); 	  				 	 	   			 	
            } 	  				 	 	   			 	
        } 	  				 	 	   			 	
        for (ContratPoudre[] contrat : devis) { 	  				 	 	   			 	
            for (int j = 0; j < 3; j++) { 	  				 	 	   			 	
                if (contrat[j].equals(demande[j])) { 	  				 	 	   			 	
                    contrat[j].setReponse(true); 	  				 	 	   			 	
                } 	  				 	 	   			 	
            } 	  				 	 	   			 	
            contrat[0].getVendeur().sendReponsePoudre(contrat, this); 	  				 	 	   			 	
        } 	  				 	 	   			 	
 	  				 	 	   			 	
 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author François Le Guernic 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    @Override 	  				 	 	   			 	
    public void sendOffrePubliqueV3(List<ContratFeveV3> offrePublique) { 	  				 	 	   			 	
        /* On achète des fèves de BQ ( seulement à équipe 2 ) et de MQ ( à équipes 2 et 3 ) aux équipes de producteur 	  				 	 	   			 	
         * Pour récupérer les offres qui nous intéressent, on stockent les informations en mémoire dans les variables 	  				 	 	   			 	
         * d'instance 	  				 	 	   			 	
         */ 	  				 	 	   			 	
        for (ContratFeveV3 c : offrePublique) { 	  				 	 	   			 	
            String vendeur = ((Acteur) c.getProducteur()).getNom(); 	  				 	 	   			 	
            int qualite = c.getQualite(); 	  				 	 	   			 	
            if (vendeur.equals("Eq2PROD") && qualite == 0) { 	  				 	 	   			 	
                contratFeveBQEq2.setOffrePublique_Quantite(c.getOffrePublique_Quantite()); 	  				 	 	   			 	
                contratFeveBQEq2.setOffrePublique_Prix(c.getOffrePublique_Prix()); 	  				 	 	   			 	
            } else if (vendeur == "Eq2PROD" && qualite == 1) { 	  				 	 	   			 	
                contratFeveMQEq2.setOffrePublique_Quantite(c.getOffrePublique_Quantite()); 	  				 	 	   			 	
                contratFeveMQEq2.setOffrePublique_Prix(c.getOffrePublique_Prix()); 	  				 	 	   			 	
            } else if (vendeur == "Eq3PROD" && qualite == 1) { 	  				 	 	   			 	
                contratFeveMQEq3.setOffrePublique_Quantite(c.getOffrePublique_Quantite()); 	  				 	 	   			 	
                contratFeveMQEq3.setOffrePublique_Prix(c.getOffrePublique_Prix()); 	  				 	 	   			 	
            } 	  				 	 	   			 	
 	  				 	 	   			 	
 	  				 	 	   			 	
        } 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author Francois Le Guernic 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    @Override 	  				 	 	   			 	
    public List<ContratFeveV3> getDemandePriveeV3() { 	  				 	 	   			 	
        /* Par convention, dans la liste de  contrats, on aura dans l'ordre : 	  				 	 	   			 	
         * - le contrat pour les fèves BQ à l'équipe 2 	  				 	 	   			 	
         * - le contrat pour les fèves MQ à l'équipe 2 	  				 	 	   			 	
         * - le contrat pour les fèves MQ à l'équipe 3 	  				 	 	   			 	
         */ 	  				 	 	   			 	
        List<ContratFeveV3> demandesPrivee = new ArrayList<ContratFeveV3>() ; 	  				 	 	   			 	
        demandesPrivee.add(this.contratFeveBQEq2) ; demandesPrivee.add(this.contratFeveMQEq2) ;demandesPrivee.add(this.contratFeveMQEq3); 	  				 	 	   			 	
        this.contratFeveBQEq2.setDemande_Prix(contratFeveBQEq2.getOffrePublique_Prix()); 	  				 	 	   			 	
        this.contratFeveBQEq2.setDemande_Quantite((int) achatsSouhaites[FEVES_BQ].getValeur()); 	  				 	 	   			 	
        this.contratFeveMQEq2.setDemande_Prix(contratFeveBQEq2.getOffrePublique_Prix()); 	  				 	 	   			 	
        this.contratFeveMQEq2.setDemande_Quantite((int) (achatsSouhaites[FEVES_MQ].getValeur() * 0.3)); 	  				 	 	   			 	
        // On répartit nos achats de MQ en 30 % à l'équipe 2 et 70 % à l'équipe 3 	  				 	 	   			 	
        this.contratFeveMQEq3.setDemande_Prix(contratFeveMQEq3.getOffrePublique_Prix()); 	  				 	 	   			 	
        this.contratFeveMQEq3.setDemande_Quantite((int) (achatsSouhaites[FEVES_MQ].getValeur() * 0.7)); 	  				 	 	   			 	
 	  				 	 	   			 	
        return demandesPrivee; 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    @Override 	  				 	 	   			 	
    public void sendContratFictifV3(List<ContratFeveV3> listContrats) { 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author François Le Guernic 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    @Override 	  				 	 	   			 	
    public void sendOffreFinaleV3(List<ContratFeveV3> offreFinale) { 	  				 	 	   			 	
        // On actualise nos trois variables d'instance avec les attributs QuantiteProposition et PrixProposition 	  				 	 	   			 	
 	  				 	 	   			 	
        for (ContratFeveV3 c : offreFinale) { 	  				 	 	   			 	
            String vendeur = ((Acteur) c.getProducteur()).getNom(); 	  				 	 	   			 	
            int qualite = c.getQualite(); 	  				 	 	   			 	
            if (vendeur.equals("Eq2PROD") && qualite == 0) { 	  				 	 	   			 	
                contratFeveBQEq2.setProposition_Quantite(c.getProposition_Quantite()); 	  				 	 	   			 	
                contratFeveBQEq2.setProposition_Prix(c.getProposition_Prix()); 	  				 	 	   			 	
            } else if (vendeur.equals("Eq2PROD") && qualite == 1) { 	  				 	 	   			 	
                contratFeveMQEq2.setProposition_Quantite(c.getProposition_Quantite()); 	  				 	 	   			 	
                contratFeveMQEq2.setProposition_Prix(c.getProposition_Prix()); 	  				 	 	   			 	
            } else if (vendeur.equals("Eq3PROD") && qualite == 1) { 	  				 	 	   			 	
                contratFeveMQEq3.setProposition_Quantite(c.getProposition_Quantite()); 	  				 	 	   			 	
                contratFeveMQEq3.setDemande_Prix(c.getProposition_Prix()); 	  				 	 	   			 	
            } 	  				 	 	   			 	
 	  				 	 	   			 	
        } 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author François Le Guernic 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    public List<ContratFeveV3> getResultVentesV3() { 	  				 	 	   			 	
        List<ContratFeveV3> listeContrat = new ArrayList<ContratFeveV3>() ; 	  				 	 	   			 	
        listeContrat.add(this.contratFeveBQEq2) ; listeContrat.add(this.contratFeveMQEq2) ; listeContrat.add(contratFeveMQEq3) ; 	  				 	 	   			 	
        for (ContratFeveV3 c : listeContrat) { 	  				 	 	   			 	
            if ((c.getProposition_Prix() <= c.getDemande_Prix()) && c.getProposition_Quantite() <= c.getDemande_Quantite()) { 	  				 	 	   			 	
                c.setReponse(true); 	  				 	 	   			 	
            } else { 	  				 	 	   			 	
                c.setReponse(false); 	  				 	 	   			 	
            } 	  				 	 	   			 	
        } 	  				 	 	   			 	
 	  				 	 	   			 	
        return listeContrat; 	  				 	 	   			 	
    } 	  				 	 	   			 	
 	  				 	 	   			 	
    /** 	  				 	 	   			 	
     * @author Maxim 	  				 	 	   			 	
     */ 	  				 	 	   			 	
    @Override 	  				 	 	   			 	
    public int getReponseBis(DemandeAO d) { 	  				 	 	   			 	
        switch (d.getQualite()) { 	  				 	 	   			 	
            case 1: { 	  				 	 	   			 	
                journal.ajouter("Eq5 renvoie MAX_VALUE à getReponse(d)"); 	  				 	 	   			 	
                return (int)Double.MAX_VALUE; 	  				 	 	   			 	
            } 	  				 	 	   			 	
            case 2: 	  				 	 	   			 	
                if (d.getQuantite() < 0.2 * stocks[FRIANDISES_MQ].getValeur()) { 	  				 	 	   			 	
                    journal.ajouter("Eq5 renvoie" + 1.1 * prix[FRIANDISES_MQ].getValeur() * d.getQuantite() + "à getReponse(d)"); 	  				 	 	   			 	
                    return (int)(1.1 * prix[FRIANDISES_MQ].getValeur() * d.getQuantite()); 	  				 	 	   			 	
                } 	  				 	 	   			 	
            case 3: { 	  				 	 	   			 	
                journal.ajouter("Eq5 renvoie MAX_VALUE à getReponse(d)"); 	  				 	 	   			 	
                return (int)Double.MAX_VALUE; 	  				 	 	   			 	
            } 	  				 	 	   			 	
            case 4: 	  				 	 	   			 	
                if (d.getQuantite() < 0.2 * stocks[TABLETTES_BQ].getValeur()) { 	  				 	 	   			 	
                    journal.ajouter("Eq5 renvoie" + 1.1 * prix[TABLETTES_BQ].getValeur() * d.getQuantite() + "à getReponse(d)"); 	  				 	 	   			 	
                    return (int)(1.1 * prix[TABLETTES_BQ].getValeur() * d.getQuantite()); 	  				 	 	   			 	
                } 	  				 	 	   			 	
            case 5: 	  				 	 	   			 	
                if (d.getQuantite() < 0.2 * stocks[TABLETTES_MQ].getValeur()) { 	  				 	 	   			 	
                    journal.ajouter("Eq5 renvoie" + 1.1 * prix[TABLETTES_MQ].getValeur() * d.getQuantite() + "à getReponse(d)"); 	  				 	 	   			 	
                    return (int)(1.1 * prix[TABLETTES_MQ].getValeur() * d.getQuantite()); 	  				 	 	   			 	
                } 	  				 	 	   			 	
            case 6: 	  				 	 	   			 	
                if (d.getQuantite() < 0.2 * stocks[TABLETTES_HQ].getValeur()) { 	  				 	 	   			 	
                    journal.ajouter("Eq5 renvoie" + 1.1 * prix[TABLETTES_HQ].getValeur() * d.getQuantite() + "à getReponse(d)"); 	  				 	 	   			 	
                    return (int)(1.1 * prix[TABLETTES_HQ].getValeur() * d.getQuantite()); 	  				 	 	   			 	
                } 	  				 	 	   			 	
        } 	  				 	 	   			 	
        return (int)Double.MAX_VALUE; 	  				 	 	   			 	
    } 	  				 	 	   			 	
     	  				 	 	   			 	
    /** @author Maxim */ 	  				 	 	   			 	
 	  				 	 	   			 	
	@Override 	  				 	 	   			 	
	public void envoyerReponseBis(int quantite, int qualite, int prix) { 	  				 	 	   			 	
		this.depenser(-prix); 	  				 	 	   			 	
		this.stocks[qualite].setValeur(this, this.stocks[qualite].getValeur()-quantite*(200/10000000)); 	  				 	 	   			 	
	} 	  				 	 	   			 	
 	  				 	 	   			 	
 	  				 	 	   			 	
} 	  				 	 	   			 	
