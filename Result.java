package project2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Result {

	public double advantage;
	public int heroID;
	
	public Result(double advantage, int heroID)
	{
		this.advantage = advantage;
		this.heroID = heroID;
	}
	
	public int getResultID()
	{
		return this.heroID;
	}
	
	public double getResultAdv()
	{
		return this.advantage;
	}

	
	public void setResultID(int heroID)
	{
		this.heroID = heroID;
	}
	
	public void setResultAdv(double advantage)
	{
		this.advantage = advantage;
	}
	
	/*
	public void setMegaMap(HashMap<Double, ArrayList<Hero>> megaMap)
	{
		this.megaMap = megaMap;
	}
	
	public HashMap<Double, ArrayList<Hero>> getMegaMap()
	{
		return this.megaMap;
	}*/
}

class SortResult implements Comparator<Result> {

	@Override
	public int compare(Result r1, Result r2) {
		return Double.compare(r1.advantage, r2.advantage);
	}
	
}
