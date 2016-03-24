public class NBody {
	
	public static void main(String[] args) {
		double t = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double time = 0;
		
		In data = new In(filename);
		int numOfPlanets = data.readInt();
		double universeRadiusSize = data.readDouble(); //radius
		
		//create array of planets
		Planet[] arrayPlanets = new Planet[numOfPlanets];
		
		//while loop through file. check if file is empty. If not, retrieve next planet
		int counter = 0;
		while (counter < numOfPlanets) {
			arrayPlanets[counter] = getPlanet(data);
			counter = counter + 1;
		}
		
		StdDraw.setScale(-universeRadiusSize, universeRadiusSize);
		StdDraw.picture(0, 0, "images/starfield.jpg");
		int index = 0;
		while (index < arrayPlanets.length) {
			StdDraw.picture(arrayPlanets[index].x, arrayPlanets[index].y, "images/"+arrayPlanets[index].img);
			index = index + 1;
		}			
		
		//add music
		StdAudio.loop("audio/2001.mid");
		
		//loop for animation
		while (time <= t) {
			for (int i1 = 0; i1 < arrayPlanets.length; i1 = i1 + 1) {
				arrayPlanets[i1].setNetForce(arrayPlanets);
			}
			for (int i2 = 0; i2 < arrayPlanets.length; i2 = i2 + 1) {
				arrayPlanets[i2].update(dt);
				arrayPlanets[i2].xNetForce = 0;
				arrayPlanets[i2].yNetForce = 0;
			}
			StdDraw.picture(0, 0, "images/starfield.jpg");
			int ind = 0;
			while (ind < arrayPlanets.length) {
				StdDraw.picture(arrayPlanets[ind].x, arrayPlanets[ind].y, "images/"+arrayPlanets[ind].img);
				ind = ind + 1;
			}			
			StdDraw.show(10);
			time = time + dt;	
		}
		
		StdOut.printf("%d\n", numOfPlanets);
		StdOut.printf("%.2e\n", universeRadiusSize);
		for (int i3 = 0; i3 < numOfPlanets; i3++) {
		    StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
		                   arrayPlanets[i3].x, arrayPlanets[i3].y, arrayPlanets[i3].xVelocity, arrayPlanets[i3].yVelocity, arrayPlanets[i3].mass, arrayPlanets[i3].img);
		}
		
		
		
	}
	
	public static Planet getPlanet(In file) {
		double x = file.readDouble();
		double y = file.readDouble();
		double velocityX = file.readDouble();
		double velocityY = file.readDouble();
		double mass = file.readDouble();
		String name = file.readString();
		
		return new Planet(x, y, velocityX, velocityY, mass, name);
	}
}