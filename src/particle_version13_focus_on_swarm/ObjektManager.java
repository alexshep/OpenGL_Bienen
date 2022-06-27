package particle_version13_focus_on_swarm;

import java.util.HashMap;

public class ObjektManager {
   private final HashMap<Integer, Agent> partikel;
   private static final ObjektManager exemplar = new ObjektManager();

   private ObjektManager() {
      partikel = new HashMap<Integer, Agent>();
   }

   public static ObjektManager getExemplar() {
      return exemplar;
   }

   public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException("Clonen ist nicht erlaubt");
   }
   
   public void registrierePartikel(Agent obj) {
      partikel.put(new Integer(obj.id), obj);
   }

   public void entfernePartikel(Agent obj) {
      partikel.remove(obj);
   }
   
   public Agent getAgent(int objID) {
      return partikel.get(new Integer(objID));
   }
   
   public HashMap<Integer, Agent> getPartikelMap() {
      return partikel;
   }
   
   public int getAgentSize() {
      return partikel.size();
   }
}
