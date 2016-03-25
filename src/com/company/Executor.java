public interface Executor {
  // Add task to execution. The result will be available through the method getValidResult(),
  // Should throw an exception in case if the method execute() was already called
  void addTask(Task task);
  
  // Add task to execution and add result validator. The result of the task should be stored into ValidResults if 
  // validator.isValid() returns true for such result. 
  // Should throw an exception in case if the method execute() was already called
  void addTask(Task task, Validator validator);
  
  // Execute all added tasks 
  void execute();
  
  // Get valid results. Should throw an exception if the method execute() has not been called
  List getValidResults();
  
  // Get invalid results. Should throw an exception if the method execute() has not been called
  List getInvalidResults();
}
