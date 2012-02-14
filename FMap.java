/************************************************************/
/*  Programmer: An Dang                                     */
/*  Email: dang.an249@gmail.com                             */
/*  Time Spent: 4 hours                                     */
/************************************************************/

// FMap<K,V> is either:
// -- EmptyMap
// -- Add( FMap, K, V )

import java.util.ArrayList ;
public abstract class FMap<K,V> {
    
    
    // emptyMap():  ->  FMap<K,V>
    
    public static <K,V>  FMap<K,V> emptyMap() {
	return new EmptyMap<K,V>() ;
    }

    // add():  K x V  ->  FMap
    public abstract FMap<K,V> add( K k0, V v0 ) ; 

    // isEmpty():      ->  boolean
    public abstract boolean isEmpty() ;

    // size():   ->  int
    public abstract int size() ;
    
    // containsKey:  K  ->  boolean
    public abstract boolean containsKey( K x ) ;

    // get:  K   ->  V
    public abstract V get( K x ) ;
						
    //toString():   ->  String
     public abstract String toString() ;

    // equals(): Object -> boolean
     public abstract boolean equals( Object x ) ;

    // hashCode():    -> int
    public abstract int hashCode() ;

}

// class of empty FMap
class EmptyMap<K,V> extends FMap<K,V>{
    
    @SuppressWarnings(value="unchecked")
    EmptyMap() {}
    public boolean isEmpty (){
	return true ;
    }

    public int size (){
	return 0 ;
    }
    
    public boolean containsKey ( K k0 ){
	return false ;
    }
    
    public V get( K k0 ) {
	return null ;
    }
    
    public FMap<K,V> add( K k0, V v0 ){
	return new Add<K,V> ( this, k0, v0 ) ;
    }

    public String toString(){
	return "{...(" + this.size() + " entries)...}" ;
    }
    
    public boolean equals( Object ob ){
	FMap temp ;
	if (! (ob instanceof FMap ) )
	    return false ;
	else{ 
	    temp = (FMap) ob ;
	    if ( temp.size() == 0 )
		return true ;
	    else 
		return false ; 
	}
    }

    public int hashCode(){
	return 924 ;
    }
    
    
}


// class of non-empty FMap
// Add<K,V>( FMap, K, V )
class Add<K,V> extends FMap<K,V> {
    
    FMap<K,V> m0 ;  // the old FMap structure    
    K k0 ;   // the new key
    V v0 ; // the new value
    
    Add( FMap<K,V> m0, K k0, V v0 ) {
	this.m0 = m0 ;
	this.k0 = k0 ;
	this.v0 = v0 ;
    } 
    
    public boolean isEmpty (){
	return false ;
    }

    public int size(){
	
	if ( m0.containsKey( k0 ) )
	    return m0.size() ;
	else 
	    return 1 + m0.size() ;
    }
    
    public boolean containsKey ( K x ){
	if ( x.equals( k0 ) )
	    return true ;
	else
	    return m0.containsKey(  x ) ;
    }
    
    
    // getMethod is implemented to get the new added key 
    // if there is two pairs that has the same key
    public V get( K x ) {
	if ( x.equals( k0 ) )
	    return v0 ;
	else 
	    return (V) m0.get( x ) ;
    }

    public FMap<K,V> add( K k0, V v0 ){
	return new Add<K,V> ( this, k0, v0 ) ;
    }

    public String toString(){
	return "{...(" + this.size() + " entries)...}" ;
    }
    
    @SuppressWarnings(value="unchecked")
    public boolean equals( Object ob ){
	
	FMap<K,V> temp ;
	if (! ( ob instanceof FMap ) )
	    return false ;
	else{
	    
	    temp = (FMap<K,V>) ob ;
	    if ( temp.isEmpty() )
		return false ;
	    else if ( samekeys( this, (Add) temp )  
		      && samepairs( this, this, (Add) temp ))
		return true ;
	    else
		return false ;
	}
    }
    
    public int hashCode(){
	K K_temp ;
	V V_temp ;
	int numKeys ;
	int hashCode = 0 ;
	ArrayList<K> currentKeys = new ArrayList<K>() ;
	getKeys(this, currentKeys) ;
	numKeys = currentKeys.size() ;

	for( int i = 0 ; i < numKeys ; i++ ){
	    K_temp = currentKeys.get(i) ;
	    V_temp = this.get( K_temp ) ;
	    
	    hashCode += (195 + 5 * (3 + K_temp.hashCode())  
			 + 7 * ( 11 + V_temp.hashCode() )) ;
	}

	return hashCode ;
    }
    
    private <K> void getKeys( Add<K,V> m, ArrayList<K> array ){
	
	if ( m.m0.isEmpty() ){
	    if ( array.contains( m.k0 ) )
		return ;
	    else{
		array.add (m.k0) ;
		return ;
	    }
	}
	else{
	    if ( array.contains( m.k0 ) )
		getKeys( (Add<K,V>) m.m0, array ) ;
	    else{
	    array.add( m.k0 ) ;
	    getKeys(  (Add<K,V>) m.m0, array ) ;
	    }
	}	
	return ;
    }
    
    // samekeys: Add x Add -> boolean
    // check if the two Adds (same size) has the same set of keys, 
    private <K,V> boolean samekeys( Add<K,V> m1 , Add<K,V> m2 ){
	
	if ( containAll( m1, m2 ) && containAll( m2, m1 ) )
	    return true ;
	else 
	    return false ;
    
    }

    // containAll: Add x Add -> boolean
    // check whether first Add contain all the key in second FMap
    private <K,V> boolean containAll( Add<K,V> m1 , Add<K,V> m2 ){
	
	//case: m2 = Add( m0 = EmptyMap, k0 , v0 )
	if ( m2.m0.isEmpty() ) {
	    if ( m1.containsKey( m2.k0 ) )
		return true ;
	    else
		return false ;
	}

	//case: m2 = Add( m0 = Add, k0, v0 )
	if ( m1.containsKey( m2.k0 ) )
	    return containAll( m1, (Add<K,V>) m2.m0 ) ;
	else
	    return false ;
    }
	

    // sameValues: Add X Add x Add -> boolean
    // check if the two maps (same size) 
    // has the same set of key-value pair
    // assumption: the two FMap already have the same keys
    // <-> but one may have dublicate keys
   
    private <K,V> boolean samepairs( Add<K,V> m1, Add<K,V> m1copy
				     , Add<K,V> m2 ){
	
	if ( m1copy.get( m1.k0 ).equals( m2.get(  m1.k0 ) ) ){
	
	    if ( m1.m0.isEmpty() )
		return true;
	    else
		return samepairs ( (Add<K,V>) m1.m0 , m1copy , m2 ) ;
	}
	else 
	    return false ;
    }
    
}
