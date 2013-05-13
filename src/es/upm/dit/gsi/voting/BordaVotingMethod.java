package es.upm.dit.gsi.voting;

import java.util.ArrayList;

import sim.app.ubik.behaviors.sharedservices.UserInterface;
import sim.app.ubik.domoticDevices.SharedService;
import sim.util.MutableInt2D;

public class BordaVotingMethod extends VotingMethod {

	public BordaVotingMethod(SharedService css) {
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
	           this.votes = this.votingConfigurations(this.css);
	           this.orderedVotes = this.orderPreferences(this.votes);	            
	           setSelectedConfiguration(configurations[this.orderedVotes.get(0).x]);      
	    }		
	}
	
	    
    /**
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

        //incializar votos con configuraciones
        for (int i = 0; i < configurations.length; i++) {
            votes.add(new MutableInt2D(i, 0));
        }
        //votar
        for (UserInterface ui : css.getUsers()) {
        	ordered = ui.getNegotiation().getOrderedPreferences(css);

            for(int i = 0; i < configurations.length; i++) {
            	ArrayList<MutableInt2D> userVotes = getUserVotes(ui);
            	votes.get(ordered.get(i).x).y += userVotes.get(ordered.get(i).x).y;	            	
            }
        }
        return votes;
    }
    
    @Override
    public ArrayList<MutableInt2D> getUserVotes(UserInterface ui){
    	
    	ArrayList<MutableInt2D> votes = new ArrayList<MutableInt2D>();
    	ArrayList<MutableInt2D> ordered = ui.getNegotiation().getOrderedPreferences(css);
    	
    	   	
    	//incializar votos con configuraciones
        for (int i = 0; i < ordered.size(); i++) {
            votes.add(new MutableInt2D(i, 0));
        }
        
        for(int i = 0; i < ordered.size(); i++) {
        	votes.get(ordered.get(i).x).y += ordered.size()-(i+1);	            	
        }
        
        return votes;
    	
    }
    
    @Override
    public ArrayList<MutableInt2D> getUserVotes(UserInterface ui, SharedService ss){
    	
    	ArrayList<MutableInt2D> votes = new ArrayList<MutableInt2D>();
    	ArrayList<MutableInt2D> ordered = ui.getNegotiation().getOrderedPreferences(ss);
    	
    	   	
    	//incializar votos con configuraciones
        for (int i = 0; i < ordered.size(); i++) {
            votes.add(new MutableInt2D(i, 0));
        }
        
        for(int i = 0; i < ordered.size(); i++) {
        	votes.get(ordered.get(i).x).y += ordered.size()-(i+1);	            	
        }
        
        return votes;
    	
    }

	

	@Override
	public String getSelectedConfiguration() {
		doVoting();
		if (echo) {
            System.out.println("Plurality VOTES ORDERED for " + this.css.getName());
            System.out.println(votesToString(this.orderedVotes, this.css));
            System.out.println("Result: " + this.css.getCurrentConfiguration());
        }
		return this.selectedConfiguration;
	}

}
