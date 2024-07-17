[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-718a45dd9cf7e7f842a935f5ebbe5719a5e09af4491e668f4dbf3b35d5cca122.svg)](https://classroom.github.com/online_ide?assignment_repo_id=15170889&assignment_repo_type=AssignmentRepo)

## How to deploy the project docker environment with one click

> @author Zuoming Yan @date 16/07/2024

First of all, ensure your project code is up-to-date, you can run code like below:

```
(base) yanzuoming@Johns-MacBook-Pro EazyinvoiceServer % git pull
```

And ensure that ports `80`, `3306`, `3000`, and `9900` are not occupied in your environment.

After that, modify the access path port in the `.env ` file as follows:

```
REACT_APP_SERVER_URL=http://localhost:8080
```

#### For macOS, follow these steps:

First of all, ensure your shell file has sufficient permissions, 

```
(base) yanzuoming@Johns-MacBook-Pro EazyinvoiceServer % chmod 755 ./deploy.sh 
```

After making the above changes, you just need to execute the `./deploy.sh` script in the current project directory!

```
(base) yanzuoming@Johns-MacBook-Pro EazyinvoiceServer % ./deploy.sh 
 ✔ Container eazyinvoiceserver-db-1        Started                                                                                                                                               0.0s 
 ✔ Container eazyinvoiceserver-platform-1  Started                                                                                                                                               0.0s 
 ✔ Container eazyinvoiceserver-frontend-1  Started                                                                                                                                               0.1s 
 ✔ Container eazyinvoiceserver-nginx-1     Started                                                                                                                                               0.0s 
(base) yanzuoming@Johns-MacBook-Pro EazyinvoiceServer % 
```

If you see the above output, it means the Docker environment has been successfully deployed!

#### For windows, follow these steps:

After making the above changes, you just need to execute the `./deploy.bat` script in the current project directory!

```
cd your_project_dict
deploy.bat
```

The output should be similar to what you would see in a macOS environment.

Last but not least,

You can directly access the project's main page using your browser at [http://localhost:80](http://localhost:80).
