package Interfaces;

import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Models.DiagnosticTest;

import java.util.List;
import java.util.Optional;

public interface IDiagnosticTestService {
    boolean addDiagnosticTest(DiagnosticTest diagnosticTest) throws DatabaseInsertException;

    Optional<DiagnosticTest> getDiagnosticTest(int testID) throws DatabaseQueryException;

    List<DiagnosticTest> getAllDiagnosticTests() throws DatabaseQueryException;
}
