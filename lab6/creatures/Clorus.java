package creatures;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;

/** An implementation the evil Cloruses that gobble up the nice vegatation.
 *  @author Mondee Lu
 */
public class Clorus extends Creature {
    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

    /** creates clorus with energy equal to E. */
    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }
	
    public Color color() {
		r = 34;
		b = 231;
		g = 0;
        return color(r, g, b);
    }


    /** Cloruses are not pacifists. */
    public void attack(Creature c) {
		double energyBoost = c.energy();
		this.energy = this.energy + energyBoost;
    }

    /* Cloruses lose 0.03 units of energy when moving
     */
    public void move() {
		this.energy = this.energy - 0.03;
    }


    /** Cloruses lose 0.01 units of energy when staying cuz metabolism */
    public void stay() {
		if (this.energy - 0.01 > 0) {
			this.energy = this.energy - 0.01;
		}
		else {
			this.energy = 0;
		}
    }

    /** Cloruses and their offspring each get 50% of the energy, with none
     *  lost to the process. Now that's efficiency! Returns a baby
     *  Clorus.
     */
    public Clorus replicate() {
        double energySplit = this.energy/2;
		this.energy = this.energy/2;
		return new Clorus(energySplit);
    }

    /** Cloruses' Rules of Behavior:
     *  1. If no empty adjacent spaces, STAY.
     *  2. Otherwise, if Plips seen, ATTACK randomly.
     *  3. Otherwise, if Clorus energy >= 1, REPLICATE to random square.
     *  4. Otherwise, MOVE
     *
     *  Returns an object of type Action. See Action.java for the
     *  scoop on how Actions work. See SampleCreature.chooseAction()
     *  for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
		List<Direction> empties = getNeighborsOfType(neighbors, "empty");
		List<Direction> plips = getNeighborsOfType(neighbors, "plip");
		
		if (empties.size() == 0) {
        	return new Action(Action.ActionType.STAY);
		}
		else if (plips.size() > 0) {
			Direction moveDir = HugLifeUtils.randomEntry(plips);
			return new Action(Action.ActionType.ATTACK, moveDir);
		}
		else if (this.energy() >= 1 && empties.size() > 0) {
			Direction moveDir = HugLifeUtils.randomEntry(empties);
			return new Action(Action.ActionType.REPLICATE, moveDir);
		}
		else {
			Direction moveDir = HugLifeUtils.randomEntry(empties);
			return new Action(Action.ActionType.MOVE, moveDir);
		}
    }
}
