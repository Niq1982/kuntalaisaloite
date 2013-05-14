package fi.om.municipalityinitiative.newdto.ui;

public class ParticipantCount {
    private final SupportNames franchise;
    private final SupportNames noFranchise;

    public ParticipantCount() {
        franchise = new SupportNames();
        noFranchise = new SupportNames();
    }

    public SupportNames getFranchise() {
        return franchise;
    }

    public SupportNames getNoFranchise() {
        return noFranchise;
    }

    public long getTotal() {
        return franchise.getTotal() + noFranchise.getTotal();
    }
    
    public long getTotalPrivateNames() {
        return franchise.getPrivateNames() + noFranchise.getPrivateNames();
    }
    
    public long getTotalPublicNames() {
        return franchise.getPublicNames() + noFranchise.getPublicNames();
    }

    public class SupportNames {

        private long publicNames = 0;
        private long privateNames = 0;

        public SupportNames() {
        }

        public long getPublicNames() {
            return publicNames;
        }

        public long getPrivateNames() {
            return privateNames;
        }

        public void setPublicNames(long publicNames) {
            this.publicNames = publicNames;
        }

        public void setPrivateNames(long privateNames) {
            this.privateNames = privateNames;
        }
        
        public long getTotal() {
            return publicNames+privateNames;
        }
    }
}
