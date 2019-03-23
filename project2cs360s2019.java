package project2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class project2cs360s2019 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scan = new Scanner(System.in);
		String inputFile = "input2.txt";
		String outputFile = "output.txt";
		
		int heroPoolCount = 0;
		String algoSelect = "";
		ArrayList<Hero> heroList = new ArrayList<Hero>();

		try {
			FileReader fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);
						
			heroPoolCount = Integer.parseInt(br.readLine());
			
			algoSelect = br.readLine();
			
			System.out.println("HERO POOL: "+heroPoolCount);
			System.out.println("ALGORITHM: "+algoSelect);
			String line = "hold";
			while(line != null)
			{
				line = br.readLine();
				if(line != null)
				{
					//System.out.println(line);
					String[] heroParts = line.split(",");
					
					int heroID = Integer.parseInt(heroParts[0]);
					double heroPower = Double.parseDouble(heroParts[1]);
					double teamMastery = Double.parseDouble(heroParts[2]);
					double oppMastery = Double.parseDouble(heroParts[3]);
					int indicator = Integer.parseInt(heroParts[4]);
					
					Hero hero = new Hero(heroID, heroPower, teamMastery, oppMastery, indicator);
					heroList.add(hero);
					hero.printHero();
				}
			}
			System.out.println(heroList.size());

			br.close();
			fr.close();
			
		} catch (FileNotFoundException fnfe) {
			System.out.println("fnfe: " + fnfe.getMessage());
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
		
		
		ArrayList<Hero> teamHeroList = new ArrayList<Hero>();
		ArrayList<Hero> oppHeroList = new ArrayList<Hero>();
		ArrayList<Hero> poolHeroList = new ArrayList<Hero>();
		
		for(int i = 0; i < heroList.size(); i++)
		{
			if(heroList.get(i).getIndicator() == 1)
			{
				teamHeroList.add(heroList.get(i));
			}
			else if(heroList.get(i).getIndicator() == 2)
			{
				oppHeroList.add(heroList.get(i));
			}
			else
			{
				poolHeroList.add(heroList.get(i));
			}
		}
		
		Collections.sort(teamHeroList, new SortID());
		
		System.out.println("Initial team: "+teamHeroList.size());
		System.out.println("Initial opponent team: "+oppHeroList.size());
		System.out.println("Initial remaining pool: "+poolHeroList.size());
		
		double currentTeamAdvantage = 0.0;
		double oppAdvantage = 0.0;
		
		Collections.sort(teamHeroList, new SortID());
		Collections.sort(oppHeroList, new SortID());
		Collections.sort(poolHeroList, new SortID());

		int minimaxOutputID = minimax(teamHeroList, oppHeroList, poolHeroList);
		
		Collections.sort(teamHeroList, new SortID());
		Collections.sort(oppHeroList, new SortID());

		System.out.println("TEAM");
		for(int i = 0; i < teamHeroList.size(); i++)
		{
			if(teamHeroList.get(i).getIndicator() == 1)
			{
				System.out.println(teamHeroList.get(i).getID()+" A: "+teamHeroList.get(i).getAdvantage());
			}
		}

		System.out.println("OPP");
		for(int i = 0; i < oppHeroList.size(); i++)
		{
			if(oppHeroList.get(i).getIndicator() == 2)
			{
				System.out.println(oppHeroList.get(i).getID()+" A: "+oppHeroList.get(i).getAdvantage());
			}
		}
		
		System.out.println("The final hero output: "+minimaxOutputID);
	}
	
	
	
	public static int minimax(ArrayList<Hero> teamHero, ArrayList<Hero> oppHero, ArrayList<Hero> poolHero)
	{
		// base case
		double terminalAdvantage = 0;
		int bestHeroID = 0;
		if(teamHero.size() == 5 && oppHero.size() == 5)
		{
			terminalAdvantage = radiantTeamCalculate(teamHero) - direTeamCalculate(oppHero);
		}
		
		terminalAdvantage = maximum(teamHero, oppHero, poolHero);

		System.out.println("terminal A: "+terminalAdvantage);
		
		ArrayList<Double> finAdvantageList = new ArrayList<Double>();
		ArrayList<Integer> finIDList = new ArrayList<Integer>();

		for(int i = 0; i < teamHero.size(); i++)
		{
			finAdvantageList.add(teamHero.get(i).getAdvantage());
		}
		
		double maxOfList = Collections.max(finAdvantageList);
		
		int count = 0;
		for(int i = 0; i < teamHero.size(); i++)
		{
			if(teamHero.get(i).getAdvantage() == maxOfList)
			{
				count++;
			}
		}

		if(count == 1)
		{
			for(int i = 0; i < teamHero.size(); i++)
			{
				if(teamHero.get(i).getAdvantage() == maxOfList)
				{
					bestHeroID = teamHero.get(i).getID();
				}
			}
		}
		else if (count >= 2)
		{
			for(int i = 0; i < teamHero.size(); i++)
			{
				if(teamHero.get(i).getAdvantage() == maxOfList)
				{
					finIDList.add(teamHero.get(i).getID());
				}
			}
			
			bestHeroID = Collections.min(finIDList);
		}
		
		return bestHeroID;
		
	}
	
	public static double maximum(ArrayList<Hero> teamHero, ArrayList<Hero> oppHero, ArrayList<Hero> poolHero)
	{
		if(teamHero.size() == 5 && oppHero.size() == 5)
		{
			return radiantTeamCalculate(teamHero) - direTeamCalculate(oppHero);
		}
		
		ArrayList<Double> advantagesFromMinimum = new ArrayList<Double>();
		
		for(int i = 0; i < poolHero.size(); i++)
		{
			Hero heroHold = poolHero.get(i);
			heroHold.setIndicator(1);
			teamHero.add(heroHold);
			heroHold.setAdvantage(radiantTeamCalculate(teamHero)-direTeamCalculate(oppHero));

			poolHero.remove(heroHold);

			advantagesFromMinimum.add(minimum(teamHero, oppHero, poolHero));
			
			if(teamHero.size() == 5 && oppHero.size() == 5)
			{
				return Collections.max(advantagesFromMinimum);
			}
		}
		
		return Collections.max(advantagesFromMinimum);
	}
	
	public static double minimum(ArrayList<Hero> teamHero, ArrayList<Hero> oppHero, ArrayList<Hero> poolHero)
	{
		if(teamHero.size() == 5 && oppHero.size() == 5)
		{
			return radiantTeamCalculate(teamHero) - direTeamCalculate(oppHero);
		}
		
		ArrayList<Double> advantagesFromMaximum = new ArrayList<Double>();

		for(int i = 0; i < poolHero.size(); i++)
		{
			Hero heroHold = poolHero.get(i);
			heroHold.setIndicator(2);
			oppHero.add(heroHold);
			heroHold.setAdvantage(radiantTeamCalculate(teamHero)-direTeamCalculate(oppHero));

			poolHero.remove(heroHold);

			advantagesFromMaximum.add(maximum(teamHero, oppHero, poolHero));
			if(teamHero.size() == 5 && oppHero.size() == 5)
			{
				return Collections.min(advantagesFromMaximum);
			}
		}
		
		return Collections.min(advantagesFromMaximum);
	}
	
	/*
	double v = Double.MAX_VALUE;
	for(int i = 0; i < 5; i++)
	{
		double value = minimax(false);
		v = Math.max(v, value);
	}
	return v;
	*/
	public static double radiantTeamCalculate(ArrayList<Hero> radiantHeroList)
	{
		int synBonus = 120;
		
		double totalAdvantage = 0;
		double heroPower = 0;
		double teamMastery = 0;
		double gain = 0;
		int heroID = 0;
		int lastDigit = 0;
		HashSet<Integer> synSet = new HashSet<Integer>();
		
		for(int i = 0; i < radiantHeroList.size(); i++)
		{
			Hero h = radiantHeroList.get(i);
			heroPower = h.getPower();
			teamMastery = h.getTeamMastery();
			
			gain = heroPower * teamMastery;
			totalAdvantage += gain;
			heroID = h.getID();
			lastDigit = heroID % 10;
			synSet.add(lastDigit);
		}
		
		if(synSet.size() >= 5)
		{
			totalAdvantage += synBonus;
		}
		
		
		return totalAdvantage;
	}
	
	public static double direTeamCalculate(ArrayList<Hero> direHeroList)
	{
		int synBonus = 120;
		
		double totalAdvantage = 0;
		double heroPower = 0;
		double oppMastery = 0;
		double gain = 0;
		int heroID = 0;
		int lastDigit = 0;
		HashSet<Integer> synSet = new HashSet<Integer>();
		
		for(int i = 0; i < direHeroList.size(); i++)
		{
			Hero h = direHeroList.get(i);
			heroPower = h.getPower();
			oppMastery = h.getOppMastery();
			
			gain = heroPower * oppMastery;
			totalAdvantage += gain;
			heroID = h.getID();
			lastDigit = heroID % 10;
			synSet.add(lastDigit);
		}
		
		if(synSet.size() >= 5)
		{
			totalAdvantage += synBonus;
		}
		
		
		return totalAdvantage;
	}

}



/*
System.out.println("TEAM");
for(int i = 0; i < teamHeroList.size(); i++)
{
	if(teamHeroList.get(i).getIndicator() == 1)
	{
		System.out.println(teamHeroList.get(i).getID());
	}
}

System.out.println("OPP");
for(int i = 0; i < oppHeroList.size(); i++)
{
	if(oppHeroList.get(i).getIndicator() == 2)
	{
		System.out.println(oppHeroList.get(i).getID());
	}
}



currentTeamAdvantage = radiantTeamCalculate(teamHeroList);
		oppAdvantage = direTeamCalculate(oppHeroList);
		System.out.println("Our calculation: "+currentTeamAdvantage);
		System.out.println("Opponent calculation: "+oppAdvantage);
		System.out.println("Our advantage (A): "+(currentTeamAdvantage - oppAdvantage));
*/




/*
HashSet<Integer> heroIDSet = new HashSet<Integer>();
HashMap<Double, Integer> advantageToHeroMap = new HashMap<Double, Integer>();

ArrayList<Hero> tempTeam = new ArrayList<Hero>();
ArrayList<Hero> tempOpp = new ArrayList<Hero>();
ArrayList<Hero> tempPool = new ArrayList<Hero>();

tempTeam = teamHero;
tempOpp = oppHero;
tempPool = poolHero;

ArrayList<Double> advantagesMiniMaxList = new ArrayList<Double>();
for(int i = 0; i < tempPool.size(); i++)
{
	Hero heroHold = tempPool.get(i);
	heroHold.setIndicator(1);
	tempTeam.add(heroHold);
	
	tempPool.remove(heroHold);
	
	double tempAdvantage = radiantTeamCalculate(tempTeam) - direTeamCalculate(tempOpp);
	int holdID = heroHold.getID();
	for(int j = 0; j < tempTeam.size(); j++)
	{
		if(tempTeam.get(j).getID() == holdID);
		{
			tempTeam.get(j).setAdvantage(tempAdvantage);
		}
	}
	
	heroIDSet.add(holdID);
	advantageToHeroMap.put(tempAdvantage, holdID);
	
	advantagesMiniMaxList.add(minimum(tempTeam, tempOpp, tempPool));
}

System.out.println("size map: "+advantageToHeroMap.size());
for(Double key : advantageToHeroMap.keySet())
{
	System.out.println("advantage: "+key+"   hero: "+advantageToHeroMap.get(key));
}

double firstMaxResult = Collections.max(advantagesMiniMaxList);


*/