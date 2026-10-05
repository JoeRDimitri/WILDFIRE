package WILDFIRE;

public class Wildfire {
    //Static counter
    private static int numberOfWildFires=0;
    //Attributes
    private final long    fireID;
    private String  fireName;
    private String  country;
    private String  region;
    private double  areaBurned;
    private double  containmentPercentage;
    private String  riskLevel;
    private boolean evacuationRequired;
    // Getters
    public long         get_fireID()                {return this.fireID;}
    public String       get_fireName()              {return this.fireName;}
    public String       get_country()               {return this.country;}
    public String       get_region()                {return this.region;}
    public double       get_areaBurned()            {return this.areaBurned;}
    public double       get_containmentPercentage() {return this.containmentPercentage;}
    public String       get_riskLevel()             {return this.riskLevel;}
    public boolean      get_evacuationRequired()    {return this.evacuationRequired;}
    public static int   get_numberOfWildFires()     {return numberOfWildFires;}
    // Setters
    // public void  set_fireID(long fid)                   {this.fireID = fid;}
    public void  set_fireName(String fn)                {this.fireName = fn;}
    public void  set_country(String co)                 {this.country = co;}
    public void  set_region(String re)                  {this.region = re;}
    public void  set_areaBurned(double ab)              {this.areaBurned = ab;}
    public void  set_containmentPercentage(double cp)   {this.containmentPercentage = cp;}
    public void  set_riskLevel(String rl)               {this.riskLevel = rl;}
    public void  set_evacuationRequired(boolean er)     {this.evacuationRequired = er;}
    // Constructors:
    public Wildfire(){
        fireID = -1;
        fireName = "N/A";
        country = "N/A";
        region = "N/A";
        areaBurned = -1.0;
        containmentPercentage = -1.0;
        riskLevel = "N/A";
        evacuationRequired = false;
        numberOfWildFires++;
    }

    public Wildfire(long fid,String fn, String co, String re, double ab, double cp, String rl, boolean er){
        this.fireID = fid;
        this.fireName = fn;
        this.country = co;
        this.region = re;
        this.areaBurned = ab;
        this.containmentPercentage = cp;
        this.riskLevel = rl;
        this.evacuationRequired = er;
        numberOfWildFires++;
    }
    //Override to string for print
    public String toString(){
        return 
        "Wildfire{"
        +"\nfireID: "   + get_fireID() 
        +"\nfireName: " + get_fireName() 
        +"\ncountry: "  + get_country()
        +"\nregion: "   + get_region()
        +"\nareaBurned: " + get_areaBurned()
        +"\ncontainmentPercentage: " + get_containmentPercentage()
        +"\nriskLevel: " + get_riskLevel()
        +"\nevacuationRequired: " + get_evacuationRequired()
        +"\n}";
    }

    //Override equals for comparaison
    public boolean equals(Object obj){
        // 1. Check for reference identity (same memory address)
        if (this == obj) {
            return true;
        }

        // 2. Check for null and ensure exact class type matches
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // 3. Cast the object to the target type
        Wildfire other = (Wildfire) obj;

        // 4. Compare fields (using Objects.equals for objects to prevent NullPointerException)
        return ( 
            get_fireID()                == other.get_fireID()                &&
            get_fireName()              == other.get_fireName()              &&
            get_country()               == other.get_country()               &&
            get_region()                == other.get_region()                &&
            get_areaBurned()            == other.get_areaBurned()            &&
            get_containmentPercentage() == other.get_containmentPercentage() &&
            get_riskLevel()             == other.get_riskLevel()             &&
            get_evacuationRequired()    == other.get_evacuationRequired()    
        );
    }
}
