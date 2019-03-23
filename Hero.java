package project2;

import java.util.Comparator;

public class Hero implements Comparator<Hero> {
	
	public int heroID;
	public double heroPower;
	public double teamMastery;
	public double oppMastery;
	public int indicator;
	public double advantage;
	
	public Hero(int heroID, double heroPower, double teamMastery, double oppMastery, int indicator)
	{
		this.heroID = heroID;
		this.heroPower = heroPower;
		this.teamMastery = teamMastery;
		this.oppMastery = oppMastery;
		this.indicator = indicator;
		this.advantage = 0;
	}
	
	public void printHero()
	{
		System.out.println();
		System.out.println("ID: "+this.heroID);
		System.out.println("POWER: "+this.heroPower);
		System.out.println("TEAM MASTERY: "+this.teamMastery);
		System.out.println("OPP MASTERY: "+this.oppMastery);
		System.out.println("INDICATOR: "+this.indicator);
		System.out.println();

	}
	
	public void setAdvantage(double advantage)
	{
		this.advantage = advantage;
	}
	
	public double getAdvantage()
	{
		return this.advantage;
	}
	
	public int getID()
	{
		return this.heroID;
	}
	
	public void setIndicator(int teamChoice)
	{
		this.indicator = teamChoice;
	}
	
	public int getIndicator()
	{
		return this.indicator;
	}

	public double getPower()
	{
		return this.heroPower;
	}
	
	public double getTeamMastery()
	{
		return this.teamMastery;
	}
	
	public double getOppMastery()
	{
		return this.oppMastery;
	}

	public int compareTo(Hero h) {
        return Integer.compare(this.getID(), h.getID());
    }

	@Override
	public int compare(Hero h1, Hero h2) {
        return Integer.compare(h1.getID(), h2.getID());
	}
	
}

class SortID implements Comparator<Hero> {

	@Override
	public int compare(Hero h1, Hero h2) {
        return Integer.compare(h1.getID(), h2.getID());
	}
	
}

