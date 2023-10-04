@echo off
cd ExpenseTrackerBackend
start cmd /k "mvn spring-boot:run"
cd ..

cd ExpenseTrackerFrontend\expense-tracker
start cmd /k "ng serve"
cd ../..

cd ExpenseTrackerUtilServer
start cmd /k "py ./main.py"
cd ..

echo All servers are starting...
pause
