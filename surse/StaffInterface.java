public interface StaffInterface {
    void addProductionSystem();
    void addActorSystem();
    void removeProductionSystem(Production name);
    void removeActorSystem(Actor name);
    void updateProduction(Production p);
    void updateActor(Actor a);
    void resolveRequest(Request request);
}
