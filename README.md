# StockWatch

1. Name: Huy Le and Stefanus Adrian
2. EID: hdl486 and sja2673
3. Email: huyle@utexas.edu and stefanusj06@gmail.com
4. Time: More than 10 hours
5. Slip days: Does Not Allow!
6. Comments: Thank you!
 
List of API:
 + Android API: Using this API, our app uses a recycler view to display a list of stocks and news, an action bar for different activities, and bottom navigation bar to move between fragments. 
 + Alpha Vantage API: Using this API, our app obtains most of the information such as all active stocks, stock overview information, stock price, top monthly portfolio, and news. Alpha Vantage gives 2 types of  responses JSON and CSV files. Therefore, we needed to learn how to parse CSV files. In addition, Alpha Vantage free key has a limitation of 5 API requests per minute and 500 requests per day which was the most challenging aspect when building and testing the app. 
 + Firestore API: Using this API, our app allows users to save their login information using email and passwords. Moreover, it stores user's favorites/watchlist stocks. Therefore, allowing users to save and retrieve the information from the cloud. 

Third-party libraries:
 + Splashscreen. This library allows the app to display a loading image when starting the app. In addition, splashscreen gives our application extra time to load data from the network in the beginning.
 + MPAndroidChart. This library allows our app to handle stock graphs. This library enables users to zoom in and out to see more details. Moreover, it has a feature  to show more stock chart details such as date, price, and volume. This library is open-source and fairly easy to use, thus we decided to use this library for our app.
 + Google Gson. This library allows the app to parse data from JSON format into List<data> in Kotlin. This library was fairly easy to use since we learned it in class.
 + CSVReader. Thí library allows the app to read data from CSV file into List<data> in Kotlin. This is new thing, we have to learn outside of class.
 + OkHttp3. This library allows the app to connect user devices and Alpha Vantage API. Similar to Gson, this library was straightforward to use as it was explained in the lecture.
 + Retrofit2. This library allows the app to send and receive data from Alpha Vantage. This tool is used together with OkHttp3.
 + Glide. This library allows the app to parse an URL and display it to the phone screen as an image. We decided to use this library since we learned it in class.

Third-party services:
 + Firebase Authentication allows users to use email and passwords to log in.
 + Firestore Database allows users to store online favorites stocks list.
 + SQLite Database allows the app to store all active stock in the phone memory. This allows the application to run faster and reduce unnecessary API calls.
