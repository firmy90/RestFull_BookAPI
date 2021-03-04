# RestFull_BookAPI

Przydatne polecenia curl do przetestowania działania serwisu:

1. curl -s -X GET 'http://localhost:8080/api/books' | json_pp
2. curl -D -s -X POST -H "Content-Type: application/json" -d '{"id":1004,"isbn":" 9780134685991","title":"Effective Java","author":"Joshua Bloch","type":"programming"}' 'http://localhost:8080/api/books'
3. curl -D -s -X PUT -H "Content-Type: application/json" -d '{"id":1004,"isbn":" 9780134685991","title":"Effective Java Third Edition","author":"Joshua Bloch","type":"programming"}' 'http://localhost:8080/api/books/1004' | json_pp
4. curl -D - -s -X DELETE 'http://localhost:8080/api/books/1004'
5. curl -D -s -X POST -H "Content-Type: application/json" -d '{"lenderUsername":"oliviajones"}' 'http://localhost:8080/api/books/1002/lendings' 
6. curl -s -X GET 'http://localhost:8080/api/books/1002/lendings' | json_pp
