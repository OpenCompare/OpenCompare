/**
 * Created by Aymeric on 10/10/2014.
 */
public class Pair<K,V> {

    private K key ;
    private V value ;

    public Pair(K _key, V _val){
        key = _key ;
        value = _val ;
    }

    public K getKey(){
        return key ;
    }

    public V getValue(){
        return value ;
    }

}
