package fi.om.municipalityinitiative.newdto.ui;

public class ParticipantCount {
    private final SupportNames rightOfVoting;
    private final SupportNames noRightOfVoting;


    public ParticipantCount() {
        rightOfVoting = new SupportNames();
        noRightOfVoting = new SupportNames();
    }

    public SupportNames getRightOfVoting() {
        return rightOfVoting;
    }

    public SupportNames getNoRightOfVoting() {
        return noRightOfVoting;
    }

    public long getTotal() {
        return rightOfVoting.getTotal() + noRightOfVoting.getTotal();
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
