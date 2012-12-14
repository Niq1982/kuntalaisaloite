package fi.om.municipalityinitiative.dto;

import java.util.List;

import com.google.common.collect.Lists;

public class InitiativePublic extends InitiativeBase {
    
    private List<AuthorInfo> initiators = Lists.newArrayList();
    
    private List<RepresentativeInfo> representatives = Lists.newArrayList();
    
    private List<RepresentativeInfo> reserves = Lists.newArrayList();


    public InitiativePublic(Long id) {
        super(id);
    }

    public InitiativePublic(InitiativeManagement initiative) {
        super(initiative);
        // FIXME: Set super class fields in super class constructor!
        assignModified(initiative.getModified());
        assignState(initiative.getState());
        assignStateDate(initiative.getStateDate());
        assignSupportCount(initiative.getSupportCount());
        
        setStartDate(initiative.getStartDate());
        assignEndDate(initiative.getEndDate());
        setProposalType(initiative.getProposalType());
        setFinancialSupport(initiative.isFinancialSupport());
        setFinancialSupportURL(initiative.getFinancialSupportURL());
        setSupportStatementsOnPaper(initiative.isSupportStatementsOnPaper());
        setSupportStatementsInWeb(initiative.isSupportStatementsInWeb());
 
        //FIXME: copy links and authors!!!
//        setLinks(...);
//        assignAuthors(...)
    }
    
    public List<AuthorInfo> getInitiators() {
        return initiators;
    }

    @Override
    public void assignAuthors(List<Author> authors) {
        for (Author author : authors) {
            if (author.isInitiator()) {
                this.initiators.add(new AuthorInfo(author));
            }
            if (author.isRepresentative()) {
                this.representatives.add(new RepresentativeInfo(author));
            } else if (author.isReserve()) {
                this.reserves.add(new RepresentativeInfo(author));
            }
        }
    }

    public List<RepresentativeInfo> getRepresentatives() {
        return representatives;
    }

    public List<RepresentativeInfo> getReserves() {
        return reserves;
    }
}
