@echo off

cd ExpenseTrackerBackend

SET PGPASSWORD=postgres
psql -U postgres -c "\q"
if %ERRORLEVEL% neq 0 (
    echo PostgreSQL is not installed or not configured correctly.
    exit /b
)

psql -U postgres -c "SELECT 1 FROM pg_database WHERE datname = 'expenseitdb'" | find "1" > nul
if %ERRORLEVEL% neq 0 (
    psql -U postgres -c "CREATE DATABASE expenseitdb"
)

start cmd /k "mvn spring-boot:run"
cd ..

cd ExpenseTrackerFrontend\expense-tracker
start cmd /k "npm install && ng serve"
cd ../..

cd ExpenseTrackerUtilServer
start cmd /k "pip install -r requirements.txt && python ./main.py"
cd ..

echo All servers are starting...
pause
