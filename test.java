package project2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class test {


	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scan = new Scanner(System.in);
		String inputFile = "input4.txt";
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
					//hero.printHero();
				}
			}

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
				
		//System.out.println("Initial team: "+teamHeroList.size());
		//System.out.println("Initial opponent team: "+oppHeroList.size());
		//System.out.println("Initial remaining pool: "+poolHeroList.size());
		
		double currentTeamAdvantage = 0.0;
		double oppAdvantage = 0.0;
		
		Collections.sort(teamHeroList, new SortID());
		Collections.sort(oppHeroList, new SortID());
		Collections.sort(poolHeroList, new SortID());


		int outputID = 0;
		//minimax(teamHeroList, oppHeroList, poolHeroList, teamHeroList.size());
	
		
		if(algoSelect.equals("minimax"))
		{
			System.out.println("MINI MAX ENTERED");
			outputID = minimax(teamHeroList, oppHeroList, poolHeroList, teamHeroList.size());
		}
		else if(algoSelect.equals("ab"))
		{
			System.out.println("AB ENTERED");
			outputID = alphabeta(teamHeroList, oppHeroList, poolHeroList, teamHeroList.size());
		}
		
		System.out.println("The final hero output: "+outputID);

		try {			
			FileWriter fw = new FileWriter(outputFile);
			PrintWriter pw = new PrintWriter(fw);
			
			pw.print(outputID);
			
			pw.flush();
			pw.close();
			fw.close();
			
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			System.out.println("fnfe: " + fnfe.getMessage());
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			System.out.println("ioe: " + ioe.getMessage());
		}
		
	}
	
	public static int alphabeta(ArrayList<Hero> teamHero, ArrayList<Hero> oppHero, ArrayList<Hero> poolHero, int position)
	{
		Result r = new Result(0, 0);
		double alpha = -Double.MAX_VALUE;
		double beta = Double.MAX_VALUE;
		r = abMaximum(teamHero, oppHero, poolHero, position, alpha, beta);

		System.out.println("terminal A: "+r.getResultAdv());
		System.out.println("r result id: "+r.getResultID());
		
		return r.getResultID();	
	}
	
	public static Result abMaximum(ArrayList<Hero> teamHero, ArrayList<Hero> oppHero, ArrayList<Hero> poolHero, int position, double alpha, double beta)
	{
		double v = -Double.MAX_VALUE;

		if(teamHero.size() == 5 && oppHero.size() == 5)
		{
			double hold = radiantTeamCalculate(teamHero) - direTeamCalculate(oppHero);

			
			Result r = new Result(hold, teamHero.get(position).getID());
			
			return r;
		}
				
		ArrayList<Result> listOfResults = new ArrayList<Result>();
		for(int i = 0; i < poolHero.size(); i++)
		{
			
			Hero heroHold = poolHero.get(i);

			heroHold.setIndicator(1);
			teamHero.add(heroHold);

			poolHero.remove(heroHold);
			v = Math.max(v, abMinimum(teamHero, oppHero, poolHero, position, alpha, beta).getResultAdv());
			
			Result rToAdd = new Result(v, heroHold.getID());
			listOfResults.add(rToAdd);

			poolHero.add(heroHold);
			heroHold.setIndicator(0);
			teamHero.remove(heroHold);
			Collections.sort(poolHero, new SortID());
			
			if(v >= beta)
			{
				//double hold = radiantTeamCalculate(teamHero) - direTeamCalculate(oppHero);
				
				//Result r = new Result(hold, teamHero.get(position).getID());
				
				//return r;
				return Collections.max(listOfResults, new SortResult());

			}
			else
			{
				alpha = Math.max(alpha, v);
			}
		}
		
		/*
		double tempA = 0;
		Result r2 = new Result(0,0);
		for(int i = 0; i < listOfResults.size(); i++)
		{
			if(listOfResults.get(i).getResultAdv() >= tempA)
			{
				tempA = listOfResults.get(i).getResultAdv();
				r2.setResultAdv(tempA);
				r2.setResultID(listOfResults.get(i).getResultID());
			}
		}
		*/
		
		return Collections.max(listOfResults, new SortResult());
	}
	
	public static Result abMinimum(ArrayList<Hero> teamHero, ArrayList<Hero> oppHero, ArrayList<Hero> poolHero, int position, double alpha, double beta)
	{
		double v = Double.MAX_VALUE;
		if(teamHero.size() == 5 && oppHero.size() == 5)
		{
			double hold = radiantTeamCalculate(teamHero) - direTeamCalculate(oppHero);

			Result r = new Result(hold, oppHero.get(position).getID());

			return r;
		}
				
		ArrayList<Result> listOfResults = new ArrayList<Result>();
		for(int i = 0; i < poolHero.size(); i++)
		{
			Hero heroHold = poolHero.get(i);

			heroHold.setIndicator(2);
			oppHero.add(heroHold);

			poolHero.remove(heroHold);
			v = Math.min(v, abMaximum(teamHero, oppHero, poolHero, position, alpha, beta).getResultAdv());
			
			Result rToAdd = new Result(v, heroHold.getID());
			listOfResults.add(rToAdd);

			poolHero.add(heroHold);
			heroHold.setIndicator(0);
			oppHero.remove(heroHold);
			Collections.sort(poolHero, new SortID());
			
			if(v <= alpha)
			{
				//double hold = radiantTeamCalculate(teamHero) - direTeamCalculate(oppHero);

				//Result r = new Result(hold, oppHero.get(position).getID());

				//return r;
				return Collections.min(listOfResults, new SortResult());

			}
			else
			{
				beta = Math.min(beta, v);
			}
			
			
			

		}
		
		/*
		double tempA = 0;
		Result r2 = new Result(0,0);

		for(int i = 0; i < listOfResults.size(); i++)
		{
			if(listOfResults.get(i).getResultAdv() <= tempA)
			{
				tempA = listOfResults.get(i).getResultAdv();
				r2.setResultAdv(tempA);
				r2.setResultID(listOfResults.get(i).getResultID());
			}
		}
		*/
		System.out.println(listOfResults.size());
		return Collections.min(listOfResults, new SortResult());
	}
	

	
	public static int minimax(ArrayList<Hero> teamHero, ArrayList<Hero> oppHero, ArrayList<Hero> poolHero, int position)
	{	
		Result r = new Result(0, 0);
		r = maximum(teamHero, oppHero, poolHero, position);

		System.out.println("terminal A: "+r.getResultAdv());
		System.out.println("r result id: "+r.getResultID());
		
		return r.getResultID();	
	}
	
	public static Result maximum(ArrayList<Hero> teamHero, ArrayList<Hero> oppHero, ArrayList<Hero> poolHero, int position)
	{
		double v = -Double.MAX_VALUE;

		if(teamHero.size() == 5 && oppHero.size() == 5)
		{
			double hold = radiantTeamCalculate(teamHero) - direTeamCalculate(oppHero);

			
			Result r = new Result(hold, teamHero.get(position).getID());
			
			return r;
		}
				
		ArrayList<Result> listOfResults = new ArrayList<Result>();
		for(int i = 0; i < poolHero.size(); i++)
		{
			
			Hero heroHold = poolHero.get(i);

			heroHold.setIndicator(1);
			teamHero.add(heroHold);

			poolHero.remove(heroHold);
			v = Math.max(v, minimum(teamHero, oppHero, poolHero, position).getResultAdv());
			Result rToAdd = new Result(v, heroHold.getID());
			listOfResults.add(rToAdd);

			poolHero.add(heroHold);
			heroHold.setIndicator(0);
			teamHero.remove(heroHold);
			Collections.sort(poolHero, new SortID());
			
		}
		
		/*
		double tempA = 0;
		Result r2 = new Result(0,0);
		for(int i = 0; i < listOfResults.size(); i++)
		{
			if(listOfResults.get(i).getResultAdv() >= tempA)
			{
				tempA = listOfResults.get(i).getResultAdv();
				r2.setResultAdv(tempA);
				r2.setResultID(listOfResults.get(i).getResultID());
			}
		}
		*/
		
		return Collections.max(listOfResults, new SortResult());
	}
	
	public static Result minimum(ArrayList<Hero> teamHero, ArrayList<Hero> oppHero, ArrayList<Hero> poolHero, int position)
	{
		double v = Double.MAX_VALUE;
		if(teamHero.size() == 5 && oppHero.size() == 5)
		{
			double hold = radiantTeamCalculate(teamHero) - direTeamCalculate(oppHero);

			Result r = new Result(hold, oppHero.get(position).getID());

			return r;
		}
				
		ArrayList<Result> listOfResults = new ArrayList<Result>();
		for(int i = 0; i < poolHero.size(); i++)
		{
			Hero heroHold = poolHero.get(i);

			heroHold.setIndicator(2);
			oppHero.add(heroHold);

			poolHero.remove(heroHold);
			v = Math.min(v, maximum(teamHero, oppHero, poolHero, position).getResultAdv());
			Result rToAdd = new Result(v, heroHold.getID());
			listOfResults.add(rToAdd);

			poolHero.add(heroHold);
			heroHold.setIndicator(0);
			oppHero.remove(heroHold);
			Collections.sort(poolHero, new SortID());
		}
		
		/*
		double tempA = 0;
		Result r2 = new Result(0,0);

		for(int i = 0; i < listOfResults.size(); i++)
		{
			if(listOfResults.get(i).getResultAdv() <= tempA)
			{
				tempA = listOfResults.get(i).getResultAdv();
				r2.setResultAdv(tempA);
				r2.setResultID(listOfResults.get(i).getResultID());
			}
		}
		*/
		return Collections.min(listOfResults, new SortResult());
	}
	
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
 * System.out.println("TEAM MAX");
			for(int i = 0; i < teamHero.size(); i++)
			{
				if(teamHero.get(i).getIndicator() == 1)
				{
					System.out.println(teamHero.get(i).getID());
				}
			}
			System.out.println("OPP MAX");
			for(int i = 0; i < oppHero.size(); i++)
			{
				if(oppHero.get(i).getIndicator() == 2)
				{
					System.out.println(oppHero.get(i).getID());
				}
			}
			
			*/

/*
 * 
 * 
 * 		System.out.println("TEAM");
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
		*/


/*
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
*/

/*
System.out.println("size of mega map: "+r.getMegaMap().size());

double yeet = 0.0;
for(double A : r.getMegaMap().keySet())
{
	System.out.println(r.getMegaMap().get(A).size());
	
	if(A >= yeet)
	{
		yeet = A;
	}
	
	if(r.getResultAdv() == A)
	{
		ArrayList<Hero> tempList = megaMap.get(A);
		for(int i = 0; i < tempList.size(); i++)
		{
			System.out.println(tempList.get(i));
		}
	}
	
}
System.out.println(yeet);
for(double A : megaMap.keySet())
{
	if(A == yeet)
	{
		ArrayList<Hero> tempList = megaMap.get(A);
		for(int i = 0; i < tempList.size(); i++)
		{
			System.out.println(tempList.get(i).getID());
		}
	}
}
*/

//System.out.println(r.getResultID());
//ArrayList<Hero> tempTeamHero = new ArrayList<Hero>();
//tempTeamHero = teamHero;
//megaMap.put(hold, tempTeamHero);
//r.getMegaMap().put(hold, tempTeamHero);
//System.out.println(r.getMegaMap().get(hold).size());

/*
if(minBase == hold)
{
	r.setResultAdv(hold);
	r.setResultID(teamHero.get(position).getID());
}

ArrayList<Hero> tempOppHero = oppHero;
megaMap.put(hold, tempOppHero);
r.getMegaMap().put(hold, tempOppHero);
*/
/*
System.out.println("START");
for(int i = 0; i < teamHero.size(); i++)
{
	System.out.println(teamHero.get(i).getID());
}
System.out.println("END");
*/