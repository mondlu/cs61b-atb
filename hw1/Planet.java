public class Planet {
	
	public double x;
	public double y;
	public double xVelocity;
	public double yVelocity;
	public double mass;
	public String img;
	public double xNetForce;
	public double yNetForce;
	public double xAccel;
	public double yAccel;
	
	//Constructor for Planet instances
	public Planet(double currentX, double currentY, double velocityInX, double velocityInY, double planetMass, String imageName) {
		x = currentX;
		y = currentY;
		xVelocity = velocityInX;
		yVelocity = velocityInY;
		mass = planetMass;
		img = imageName;
	}
	
	//Method to calculate distance between two planets
	public double calcDistance(Planet other) {
		double xCoordDiff = other.x - x;
		double yCoordDiff = other.y - y;
		double distanceValue = Math.sqrt((xCoordDiff*xCoordDiff)+(yCoordDiff*yCoordDiff));
		return distanceValue;
	}
	
	//Method to calculate pairwise force
	public double calcPairwiseForce(Planet other) {
		double g = 6.67e-11;
		double massOther = other.mass;
		double distance = calcDistance(other);
		double forceValue = g*((mass*massOther)/(distance*distance));
		return forceValue;
		
	}
	
	//Method to calculate Pairwise Force X
	public double calcPairwiseForceX(Planet other) {
		double fx = calcPairwiseForce(other);
		double dx = other.x - x;
		double rx = calcDistance(other);
		double forceValueX = fx*(dx/rx);
		return forceValueX;
		
	}
	
	//Method to calcualte Pairwise Force Y
	public double calcPairwiseForceY(Planet other) {
		double fy = calcPairwiseForce(other);
		double dy = other.y - y;
		double ry = calcDistance(other);
		double forceValueY = fy*(dy/ry);
		return forceValueY;
		
	}
	
	//Method to Calcualte the net force exerted on the Planet
	public void setNetForce(Planet[] planetArray) {
		int index = 0;
		while (index < planetArray.length) {
			if (planetArray[index] == this) {
				index = index + 1;
			} else {
				xNetForce = xNetForce + calcPairwiseForceX(planetArray[index]);
				yNetForce = yNetForce + calcPairwiseForceY(planetArray[index]);
				index = index + 1;
			}
		}
	}
	
	//Method to draw planet on x,y coordinate 
	public void draw() {
		StdDraw.picture(x, y, img);
	}
	
	//Method to update position and velocities of the Planets
	public void update(double dt) {
		//calculate and set acceleration
		xAccel = xNetForce/ mass;
		yAccel = yNetForce/ mass;
		//calculate and set new velocity
		xVelocity = xVelocity + (dt*xAccel);
		yVelocity = yVelocity + (dt*yAccel);
		//calculate and set new position
		x = x + (dt*xVelocity);
		y = y + (dt*yVelocity);
	}
	
	
}