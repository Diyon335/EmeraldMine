import java.util.Scanner;

/**
 * @author Diyon
 * This abstract class acts as a parent/base for every child class that extends it. All objects created below share these characteristics unless overridden
 */
abstract class WorldObject {
    //returns true if this object is edible, false otherwise
    public abstract boolean isEdible();
    //returns true if the object can fall (due to gravity), false otherwise.
    public abstract boolean hasMass();
    //returns true if the object gets destroyed if a rock falls on it, false otherwise
    public abstract boolean isVulnerable();

    public boolean canMove(){
        return false;
    }

    public boolean isPlayer(){
        return false;
    }
    //for a monster's attack
    public char getMove(){
        return '?';
    }

    public int getEmeraldValue(){
        return 0;
    }

    public String toString(){
        return "";
    }
}

/**
 * These are the subclasses of the abstract class WorldObject
 */
class Space extends WorldObject{

    public Space(){

    }
    //space is edible
    @Override
    public boolean isEdible() {
        return true;
    }
    //space has no mass
    @Override
    public boolean hasMass() {
        return false;
    }
    //space is vulnerable
    @Override
    public boolean isVulnerable() {
        return true;
    }
    //overrides the WorldObject's toString from null > "."
    public String toString(){

        return super.toString()+".";
    }
}

class Rock extends WorldObject{

    public Rock(){

    }

    //rocks are not edible
    @Override
    public boolean isEdible() {
        return false;
    }

    //rocks have mass
    @Override
    public boolean hasMass(){
        return true;
    }
    //rocks are not vulnerable
    @Override
    public boolean isVulnerable() {
        return false;
    }
    //overrides the WorldObject's toString from null > "r" for rock
    public String toString(){

        return super.toString()+"r";
    }
}

/**
 * This class exists to override the isEdible method to true
 */
abstract class EdibleObject extends WorldObject{

    @Override
    public boolean isEdible() {
        return true;
    }
}
/**
 * this class exists to override the canMove method to true
 */
abstract class Moveable extends WorldObject{

    @Override
    public boolean canMove() {
        return true;
    }
}

class Dirt extends EdibleObject{

    //Dirt is edible. no need to override again because it inherits isEdible = true from EdibleObject

    //Dirt has no mass. must override has EdibleObject does not do this
    @Override
    public boolean hasMass() {
        return false;
    }

    //Dirt is not vulnerable
    @Override
    public boolean isVulnerable() {
        return false;
    }

    @Override
    public String toString() {
        return super.toString()+"#";
    }
}

class Emerald extends EdibleObject{

    //Emeralds are edible.

    //Emeralds have mass. so must override
    @Override
    public boolean hasMass() {
        return true;
    }

    //Emeralds are not vulnerable, so must override
    @Override
    public boolean isVulnerable() {
        return false;
    }
    //Emeralds have a value of 1. Default is 0, so must +1
    @Override
    public int getEmeraldValue() {
        return super.getEmeraldValue()+1;
    }

    @Override
    public String toString() {
        return super.toString()+"e";
    }
}

class Diamond extends EdibleObject{

    //Diamonds have mass. so must override
    @Override
    public boolean hasMass() {
        return true;
    }

    //Diamonds are vulnerable, so must override
    @Override
    public boolean isVulnerable() {
        return true;
    }

    //Diamonds have a value of 3 more than the emeralds. Default is 0, so must +3
    @Override
    public int getEmeraldValue() {
        return super.getEmeraldValue()+3;
    }

    @Override
    public String toString() {
        return super.toString()+"d";
    }
}

class Alien extends Moveable{

    //aliens are not edible
    @Override
    public boolean isEdible() {
        return false;
    }

    //no mass
    @Override
    public boolean hasMass() {
        return false;
    }

    //players are not vulnerable
    @Override
    public boolean isVulnerable() {
        return true;
    }

    //should override getMove
    @Override
    public char getMove() {

        //indicates direction of movement. u = up, d= down, .. etc
        String direction = "udlr";
        //this generates a random number from 0-3 (4 different numbers). will choose either u, d, l or r randomly
        int i = (int) (Math.random()*4);

        return direction.charAt(i);
    }

    //aliens are represented by "A"
    @Override
    public String toString() {
        return super.toString()+"a";
    }
}

class Player extends WorldObject{

    //players are not edible
    @Override
    public boolean isEdible() {
        return false;
    }

    //no mass
    @Override
    public boolean hasMass() {
        return false;
    }

    //players are vulnerable
    @Override
    public boolean isVulnerable() {
        return true;
    }

    //overrides isPlayer to true to say it is a player
    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public char getMove() {
        Scanner input = new Scanner (System.in);
        //player inputs a single letter string (w, a, s or d) and it'll read that and return this as the player move
        return input.next().charAt(0);
    }

    //players are represented by "P"
    @Override
    public String toString() {
        return super.toString()+"p";
    }
}

