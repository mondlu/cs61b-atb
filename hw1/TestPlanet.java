public class TestPlanet {
	
	public static void main(String[] args) {
		checkTestPlanet();
	}
	

	
	private static void checkTestPlanet() {
		
		Planet jupitar = new Planet(1.0, 1.0, 3.0, 4.0, 5.0, "jupitar.gif");
		Planet mars = new Planet(4.0, 5.0, 3.0, 4.0, 5.0, "mars.gif");
		
		double pairWiseX = jupitar.calcPairwiseForceX(mars);
		double pairWiseY = jupitar.calcPairwiseForceY(mars);
		
		System.out.println("Pairwise Force X: " + pairWiseX);
		System.out.println("Pairwise Force Y: " + pairWiseY);
	}
}
