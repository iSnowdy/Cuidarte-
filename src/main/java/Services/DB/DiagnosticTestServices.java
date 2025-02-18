package Services.DB;

import DAO.DiagnosticTestDAO;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseOpeningException;
import Exceptions.DatabaseQueryException;
import Interfaces.IDiagnosticTestService;
import Models.DiagnosticTest;

import java.util.List;
import java.util.Optional;

public class DiagnosticTestServices implements IDiagnosticTestService {
    private final DiagnosticTestDAO diagnosticTestDAO;

    public DiagnosticTestServices() throws DatabaseOpeningException {
        this.diagnosticTestDAO = new DiagnosticTestDAO();
    }


    @Override
    public boolean addDiagnosticTest(DiagnosticTest diagnosticTest) throws DatabaseInsertException {
        return diagnosticTestDAO.save(diagnosticTest);
    }

    @Override
    public Optional<DiagnosticTest> getDiagnosticTest(int testID) throws DatabaseQueryException {
        return diagnosticTestDAO.findById(testID);
    }

    @Override
    public List<DiagnosticTest> getAllDiagnosticTests() throws DatabaseQueryException {
        return diagnosticTestDAO.findAll();
    }
}
