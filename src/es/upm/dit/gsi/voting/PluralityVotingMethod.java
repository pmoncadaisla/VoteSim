package es.upm.dit.gsi.voting;

import java.util.ArrayList;

import sim.app.ubik.behaviors.sharedservices.UserInterface;
import sim.app.ubik.domoticDevices.SharedService;
import sim.util.MutableInt2D;

public class PluralityVotingMethod extends VotingMethod {

	public PluralityVotingMethod(SharedService css) {
		super(css);
	}	

	/**
	 * This method implements the moting method and writes the selected configuration
	 * into selectedConfiguration.
	 * For this rankingVoting it orders the votes and gets the first one.
	 */
	public void doVoting() {
		if (this.getUsersSize()> 1) {
	           String configurations[] = this.css.getConfigurations();
	           
	           // Se realizan los votos a cada servicio según el algoritmo.
	           this.votes = this.votingConfigurations(this.css);
	           
	           // Se ordenan los votos
	           this.orderedVotes = this.orderPreferences(this.votes);	            
	           setSelectedConfiguration(configurations[this.orderedVotes.get(0).x]);      
	    }		
	}	
	    
    /**
     * Es el método que se encarga de realizar las votaciones.
     * Va recorriendo las configuraciones de preferencias de cada uno de los usuarios y va acumulando la suma del valor 
     * que especifica para cada uno de los servicios.
     * Devuelve los votos que recibe cada servicio en un array de Int2D, siendo
     * x el índice de la configuración del serivicio e y los votos recibidos.
     *
     * @param css
     * @return
     */
    @Override
    protected ArrayList<MutableInt2D> votingConfigurations(SharedService css) {

        String configurations[] = css.getConfigurations();
        ArrayList<MutableInt2D> votes = new ArrayList<MutableInt2D>();
        
        ArrayList<MutableInt2D> ordered = new ArrayList<MutableInt2D>();

        for (int i = 0; i < configurations.length; i++) {
            votes.add(new MutableInt2D(i, 0));
        }
        
        /* Se cogen los votos de cada usuario y se suman en las votaciones globales */
        for (UserInterface ui : css.getUsers()) {
            for (int i = 0; i < configurations.length; i++) {            
            	ArrayList<MutableInt2D> userVotes = getUserVotes(ui);
            	votes.get(ordered.get(i).x).y += userVotes.get(ordered.get(i).x).y;	
            }
        }
        return votes;
    }
    
    /**
     * Devuelve los votos con un 1 en la preferencia más alta del usuario y 0 en el resto.
     * @param ui
     * @return
     */
    public ArrayList<MutableInt2D> getUserVotes(UserInterface ui){
    	
    	ArrayList<MutableInt2D> votes = new ArrayList<MutableInt2D>();
    	ArrayList<MutableInt2D> ordered = ui.getNegotiation().getOrderedPreferences(css);

        for (int i = 0; i < ordered.size(); i++) {
            votes.add(new MutableInt2D(i, 0));
        }   
       
        ordered = ui.getNegotiation().getOrderedPreferences(css);
        votes.get(ordered.get(0).x).y = 1;    
        
        return votes;    	
    }		
	

	@Override
	public String getSelectedConfiguration() {
		doVoting();
		if (echo) {
			log.finest("Plurality VOTES ORDERED for " + this.css.getName());
			log.finest(votesToString(this.orderedVotes, this.css));
			log.finest("Result: " + this.css.getCurrentConfiguration());
        }
		return this.selectedConfiguration;
	}

}
