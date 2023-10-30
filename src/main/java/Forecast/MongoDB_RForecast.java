package Forecast;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;

import java.util.ArrayList;

public class MongoDB_RForecast {
    private String mongo_host;
    private int mongo_port;
    private String mongo_database;
    private String mongo_user;
    private String mongo_pwd;
    private String rserve_host;
    private int rserve_port;
    private RConnection rconnection = null;
    private String Rfunctions_path;

    /**
     * Connects with RServe and sources the script's functions to R
     * @param mongo_host: IP or domain name of the MongoDB instance containing the data
     * @param mongo_port: Port of the MongoDB instance containing the data
     * @param mongo_database: Name of the MongoDB database containing the data
     * @param mongo_user: Username to access the MongoDB instance (to use if MongoDB is protected with basic auth)
     * @param mongo_pwd: Password to access the MongoDB instance (to use if MongoDB is protected with basic auth)
     * @param Rfunctions_path: (On Rserve side) Path to the R scripts' file
     * @throws REngineException
     * @throws REXPMismatchException
     */
    public MongoDB_RForecast(String mongo_host, int mongo_port, String mongo_database, String mongo_user,
                             String mongo_pwd, String Rfunctions_path)
            throws REngineException, REXPMismatchException {
        this.mongo_host = mongo_host;
        this.mongo_port = mongo_port;
        this.mongo_database = mongo_database;
        this.mongo_user = mongo_user;
        this.mongo_pwd = mongo_pwd;
        this.rserve_host = "127.0.0.1";
        this.rserve_port = 6311;
        this.Rfunctions_path = Rfunctions_path;

        this.initializeR();
    }

    /**
     * Connects with RServe and sources the script's functions to R
     * @param mongo_host: IP or domain name of the MongoDB instance containing the data
     * @param mongo_port: Port of the MongoDB instance containing the data
     * @param mongo_database: Name of the MongoDB database containing the data
     * @param mongo_user: Username to access the MongoDB instance (to use if MongoDB is protected with basic auth)
     * @param mongo_pwd: Password to access the MongoDB instance (to use if MongoDB is protected with basic auth)
     * @param rserve_host: IP or domain name of RServe. Default is localhost
     * @param rserve_port: Port to use to access RServe. Default is 6311
     * @param Rfunctions_path: (On Rserve side) Path to the R scripts' file
     * @throws REXPMismatchException
     * @throws REngineException
     */
    public MongoDB_RForecast(String mongo_host, int mongo_port, String mongo_database, String mongo_user,
                             String mongo_pwd, String rserve_host, int rserve_port, String Rfunctions_path)
            throws REXPMismatchException, REngineException {
        this.mongo_host = mongo_host;
        this.mongo_port = mongo_port;
        this.mongo_database = mongo_database;
        this.mongo_user = mongo_user;
        this.mongo_pwd = mongo_pwd;
        this.rserve_host = rserve_host;
        this.rserve_port = rserve_port;
        this.Rfunctions_path = Rfunctions_path;

        this.initializeR();
    }

    /**
     *
     * Connects with RServe and sources the script's functions to R
     * @param mongo_host: IP or domain name of the MongoDB instance containing the data
     * @param mongo_port: Port of the MongoDB instance containing the data
     * @param mongo_database: Name of the MongoDB database containing the data
     * @param Rfunctions_path: (On Rserve side) Path to the R scripts' file
     * @throws REXPMismatchException
     * @throws REngineException
     */
    public MongoDB_RForecast(String mongo_host, int mongo_port, String mongo_database, String Rfunctions_path)
            throws REXPMismatchException, REngineException {
        this.mongo_host = mongo_host;
        this.mongo_port = mongo_port;
        this.mongo_database = mongo_database;
        this.mongo_user = "";
        this.mongo_pwd = "";
        this.rserve_host = "127.0.0.1";
        this.rserve_port = 6311;
        this.Rfunctions_path = Rfunctions_path;

        this.initializeR();
    }

    /**
     * Connects with RServe and sources the script's functions to R
     * @param mongo_host: IP or domain name of the MongoDB instance containing the data
     * @param mongo_port: Port of the MongoDB instance containing the data
     * @param mongo_database: Name of the MongoDB database containing the data
     * @param rserve_host: IP or domain name of RServe. Default is localhost
     * @param rserve_port: Port to use to access RServe. Default is 6311
     * @param Rfunctions_path: (On Rserve side) Path to the R scripts' file
     * @throws REXPMismatchException
     * @throws REngineException
     */
    public MongoDB_RForecast(String mongo_host, int mongo_port, String mongo_database,
                             String rserve_host, int rserve_port, String Rfunctions_path)
            throws REXPMismatchException, REngineException {
        this.mongo_host = mongo_host;
        this.mongo_port = mongo_port;
        this.mongo_database = mongo_database;
        this.mongo_user = "";
        this.mongo_pwd = "";
        this.rserve_host = rserve_host;
        this.rserve_port = rserve_port;
        this.Rfunctions_path = Rfunctions_path;

        this.initializeR();
    }

    private void initializeR() throws REXPMismatchException, REngineException {
        this.rconnection = new RConnection(this.rserve_host, this.rserve_port);
        sourceTimeSeriesFunctions();
        initMongoDBConnection();
    }

    /**
     * Closes the connection to RServe
     */
    public void close() {
        this.rconnection.close();
    }

    private void sourceTimeSeriesFunctions() throws REXPMismatchException, REngineException {
        String sourceFunctionsStatement = "source(\"" + this.Rfunctions_path + "\")";
        Common.evaluateR(this.rconnection, sourceFunctionsStatement);
        System.out.println("TIME SERIES FUNCTIONS SOURCED");// + rResponseObject.asString());
    }

    private void initMongoDBConnection() throws REXPMismatchException, REngineException {
        String mongodbSearchConnection = "mongoDBConnection(host = \"" + this.mongo_host + "\"," +
                " database = \"" + this.mongo_database + "\"," +
                " username = \"" + this.mongo_user + "\"," +
                " password = \"" + this.mongo_pwd + "\"," +
                " port = " + String.valueOf(this.mongo_port) + ")";

        Common.evaluateR(this.rconnection, mongodbSearchConnection);
        System.out.println("MONGODB SEARCH CONNECTION SUCCESSFUL");// + rResponseObject.asString());
    }

    /**
     * Get the names of the forecasting techniques available
     * @return Common.ForecastingTechnique[]
     */
    public Common.ForecastTechnique[] getForecastTechniques () throws REXPMismatchException, REngineException {
        String[] scriptAvailableMethods = Common.getAvailableMethods(this.rconnection);
        Common.ForecastTechnique[] availableToReturn = new Common.ForecastTechnique[scriptAvailableMethods.length];
        for (int i = 0; i < scriptAvailableMethods.length; i++) {
            availableToReturn[i] = Common.ForecastTechnique.valueOf(scriptAvailableMethods[i]);
        }
        return availableToReturn;
    }

    /**
     * Fits multiple forecast models using a single forecasting technique and saves them to disk. This function should
     * be called periodically in order to keep the saved models up-to-date with recent data.
     * @param elementNames: array containing the elements for which the forecasting models will be fitted
     * @param index: MongoDB collection containing the elements of "elementNames" array
     * @param tsFrequency: The value of argument frequency is used when the series is sampled an integral number
     *                  of times in each unit time interval. For example, one could use a value of 7 for frequency
     *                  when the data are sampled daily, and the natural time period is a week, or 12 when the data
     *                  are sampled monthly and the natural time period is a year. Values of 4 and 12 are assumed in
     *                  (e.g.) print methods to imply a quarterly and monthly series respectively.
     *                  Source: "ts" R documentation
     * @param technique: Technique to be used to fit the forecasting model. To be defined using the enum
     *                 "Common.ForecastTechnique"
     * @throws REXPMismatchException
     * @throws REngineException
     */
    public String[] multipleElementTrain(String[] elementNames, String index, String tsFrequency,
                                         Common.ForecastTechnique technique)
            throws REXPMismatchException, REngineException {
        String[] trainResult = new String[elementNames.length];

        for (int i = 0; i < elementNames.length; i++) {
            try {
                trainForecastModel(elementNames[i], index, tsFrequency, technique);
                trainResult[i] = "OK";
            } catch (ArithmeticException e) {
                trainResult[i] = e.getMessage();
            }
        }
        return trainResult;
    }

    /**
     * Forecasts multiple models using a single forecasting technique. For every element, the method will check if there
     * exists a saved fitted model with the chosed technique. If it's found, it will be loaded and used to perform the
     * forecast. If not, the model will be fitted and saved to disk prior to the forecasting.
     * @param elementNames: array containing the elements for which the forecasting models will be fitted
     * @param index: MongoDB collection containing the elements of "elementNames" array
     * @param tsFrequency: The value of argument frequency is used when the series is sampled an integral number
     *                  of times in each unit time interval. For example, one could use a value of 7 for frequency
     *                  when the data are sampled daily, and the natural time period is a week, or 12 when the data
     *                  are sampled monthly and the natural time period is a year. Values of 4 and 12 are assumed in
     *                  (e.g.) print methods to imply a quarterly and monthly series respectively.
     *                  Source: "ts" R documentation
     * @param horizon: Number of periods to forecast. If working with daily data, it will correspond to the number
     *                  of days to forecast
     * @param technique: Technique to be used to fit the forecasting model. To be defined using the enum
     *                 "Common.ForecastTechnique"
     * @throws REXPMismatchException
     * @throws REngineException
     */
    public ArrayList<ForecastDTO> multipleElementForecast(String[] elementNames, String index, String tsFrequency,
                                                          String horizon, Common.ForecastTechnique technique)
            throws REXPMismatchException, REngineException {
        ArrayList<ForecastDTO> forecasts = new ArrayList<>();
        for (String elementName : elementNames) {
            forecasts.add(forecast(elementName, index, tsFrequency, horizon, technique));
        }
        return forecasts;
    }

    /**
     * Fits an individual forecasting model using the selected forecasting technique and saves the model to disk.
     * This function should be called periodically in order to keep the saved model up-to-date with recent data.
     * @param elementName: Element for which the forecasting model will be fitted
     * @param index: MongoDB collection containing the element to forecast ("elementName")
     * @param tsFrequency: The value of argument frequency is used when the series is sampled an integral number
     *                  of times in each unit time interval. For example, one could use a value of 7 for frequency
     *                  when the data are sampled daily, and the natural time period is a week, or 12 when the data
     *                  are sampled monthly and the natural time period is a year. Values of 4 and 12 are assumed in
     *                  (e.g.) print methods to imply a quarterly and monthly series respectively.
     *                  Source: "ts" R documentation
     * @param technique: Technique to be used to fit the forecasting model. To be defined using the enum
     *                 "Common.ForecastTechnique"
     * @throws REXPMismatchException
     * @throws REngineException
     */
    public void trainForecastModel(String elementName, String index, String tsFrequency,
                                 Common.ForecastTechnique technique) throws REXPMismatchException, REngineException {
        switch (technique) {
                case ARIMA:
                    System.out.println("FITTING ARIMA MODEL");
                    trainArimaModel(elementName, index, "FALSE", tsFrequency);
                    break;
                case ARIMA_FORCE_SEASONALITY:
                    System.out.println("FITTING ARIMA MODEL, FORCING SEASONALITY");
                    trainArimaModel(elementName, index, "TRUE", tsFrequency);
                    break;
                case THETA:
                    System.out.println("FITTING THETA MODEL");
                    trainThetaModel(elementName, index, tsFrequency);
                    break;
                case ETS:
                    System.out.println("FITTING ETS MODEL");
                    trainETSModel(elementName, index, "FALSE", tsFrequency);
                    break;
                case ETSDAMPED:
                    System.out.println("FITTING ETS DAMPED MODEL");
                    trainETSModel(elementName, index, "TRUE", tsFrequency);
                    break;
                case BAGGEDETS:
                    System.out.println("FITTING BAGGED ETS MODEL (THIS CAN TAKE A WHILE)");
                    trainBaggedETSModel(elementName, index, tsFrequency);
                    break;
                case STL:
                    System.out.println("FITTING STL MODEL");
                    trainSTLModel(elementName, index, tsFrequency);
                    break;
                case NN:
                    System.out.println("FITTING NEURAL NETWORK (NNETA) MODEL");
                    trainNNModel(elementName, index, tsFrequency);
                    break;
                case HYBRID:
                    System.out.println("FITTING HYBRID MODEL (THIS CAN TAKE A WHILE)");
                    trainHybridModel(elementName, index, "10", tsFrequency);
                    break;
                default:
                case PROPHET:
                    System.out.println("FITTING PROPHET MODEL");
                    trainProphetModel(elementName, index);
                break;
        }
    }

    /**
     * Forecasts using the selected forecasting technique. The method will check if there exists a saved fitted model
     * with the chosed technique. If it's found, it will be loaded and used to perform the forecast. If not, the model
     * will be fitted and saved to disk prior to the forecasting.
     * @param elementName: Element for which the forecasting model will be fitted
     * @param index: MongoDB collection containing the element to forecast ("elementName")
     * @param tsFrequency: The value of argument frequency is used when the series is sampled an integral number
     *                  of times in each unit time interval. For example, one could use a value of 7 for frequency
     *                  when the data are sampled daily, and the natural time period is a week, or 12 when the data
     *                  are sampled monthly and the natural time period is a year. Values of 4 and 12 are assumed in
     *                  (e.g.) print methods to imply a quarterly and monthly series respectively.
     *                  Source: "ts" R documentation
     * @param horizon: Number of periods to forecast. If working with daily data, it will correspond to the number
     *                  of days to forecast
     * @param technique: Technique to be used to fit the forecasting model. To be defined using the enum
     *                 "Common.ForecastTechnique"
     * @throws REXPMismatchException
     * @throws REngineException
     */
    public ForecastDTO forecast(String elementName, String index, String tsFrequency, String horizon,
                                      Common.ForecastTechnique technique)
            throws REXPMismatchException, REngineException {
        switch (technique) {
            case ARIMA:
                System.out.println("FORECASTING WITH ARIMA MODEL");
                return forecastArimaModel(elementName, index, "FALSE", tsFrequency, horizon);
            case ARIMA_FORCE_SEASONALITY:
                System.out.println("FORECASTING WITH ARIMA MODEL, FORCING SEASONALITY");
                return forecastArimaModel(elementName, index, "TRUE", tsFrequency, horizon);
            case THETA:
                System.out.println("FORECASTING WITH THETA MODEL");
                return forecastThetaModel(elementName, index, tsFrequency, horizon);
            case ETS:
                System.out.println("FORECASTING WITH ETS MODEL");
                return forecastETSModel(elementName, index, "FALSE", tsFrequency, horizon);
            case ETSDAMPED:
                System.out.println("FORECASTING WITH ETS DAMPED MODEL");
                return forecastETSModel(elementName, index, "TRUE", tsFrequency, horizon);
            case BAGGEDETS:
                System.out.println("FORECASTING WITH BAGGED ETS MODEL");
                return forecastBaggedETSModel(elementName, index, tsFrequency, horizon);
            case STL:
                System.out.println("FORECASTING WITH STL MODEL");
                return forecastSTLModel(elementName, index, tsFrequency, horizon);
            case NN:
                System.out.println("FORECASTING WITH NEURAL NETWORK (NNETA) MODEL (THIS CAN TAKE A WHILE)");
                return forecastNNModel(elementName, index, tsFrequency, horizon);
            case HYBRID:
                System.out.println("FORECASTING WITH HYBRID MODEL");
                return forecastHybridModel(elementName, index, tsFrequency, horizon);
            default:
            case PROPHET:
                System.out.println("FORECASTING WITH PROPHET MODEL");
                return forecastProphetModel(elementName, index, horizon);
        }
    }

    private void trainArimaModel(String elementName, String index, String forceSeasonality, String frequencyts)
            throws REXPMismatchException, REngineException {
        String arimaTrainCall = "trainArimaModel(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "forceSeasonality = " + forceSeasonality + ", frequencyts = " + frequencyts + ")";

        Common.evaluateR(this.rconnection, arimaTrainCall);
    }

    private ForecastDTO forecastArimaModel(String elementName, String index, String forceSeasonality,
                                           String frequencyts, String tsForecastHorizon)
            throws REXPMismatchException, REngineException {
        String arimaForecastCall = "forecastArimaWrapper(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "forceSeasonality = " + forceSeasonality + ", frequencyts = " + frequencyts + ", " +
                "horizon = " + tsForecastHorizon + ")";

        return Common.evaluateRforecast(this.rconnection, arimaForecastCall, elementName);
    }

    private void trainThetaModel(String elementName, String index, String frequencyts)
            throws REXPMismatchException, REngineException {
        String thetaTrainCall = "trainThetaModel(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "frequencyts = " + frequencyts + ")";

        Common.evaluateR(this.rconnection, thetaTrainCall);
    }

    private ForecastDTO forecastThetaModel(String elementName, String index, String frequencyts,
                                           String tsForecastHorizon)  throws REXPMismatchException, REngineException {
        String thetaForecastCall = "forecastThetaWrapper(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "frequencyts = " + frequencyts + ", " + "horizon = " + tsForecastHorizon + ")";

        return Common.evaluateRforecast(this.rconnection, thetaForecastCall, elementName);
    }

    private void trainETSModel(String elementName, String index, String forceDamped, String frequencyts)
            throws REXPMismatchException, REngineException {
        String etsTrainCall = "trainETSModel(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "forceDamped = " + forceDamped + ", frequencyts = " + frequencyts + ")";

        Common.evaluateR(this.rconnection, etsTrainCall);
    }

    private ForecastDTO forecastETSModel(String elementName, String index, String forceDamped,
                                           String frequencyts, String tsForecastHorizon)
            throws REXPMismatchException, REngineException {
        String etsForecastCall = "forecastETSWrapper(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "forceDamped = " + forceDamped + ", frequencyts = " + frequencyts + ", " +
                "horizon = " + tsForecastHorizon + ")";

        return Common.evaluateRforecast(this.rconnection, etsForecastCall, elementName);
    }

    private void trainBaggedETSModel(String elementName, String index, String frequencyts)
            throws REXPMismatchException, REngineException {
        String baggedETSTrainCall = "trainBaggedETSModel(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "frequencyts = " + frequencyts + ")";

        Common.evaluateR(this.rconnection, baggedETSTrainCall);
    }

    private ForecastDTO forecastBaggedETSModel(String elementName, String index, String frequencyts,
                                           String tsForecastHorizon)  throws REXPMismatchException, REngineException {
        String baggedETSForecastCall = "forecastBaggedETSWrapper(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "frequencyts = " + frequencyts + ", " + "horizon = " + tsForecastHorizon + ")";

        return Common.evaluateRforecast(this.rconnection, baggedETSForecastCall, elementName);
    }

    private void trainSTLModel(String elementName, String index, String frequencyts)
            throws REXPMismatchException, REngineException {
        String STLModelTrainCall = "trainSTLModel(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "frequencyts = " + frequencyts + ")";

        Common.evaluateR(this.rconnection, STLModelTrainCall);
    }

    private ForecastDTO forecastSTLModel(String elementName, String index, String frequencyts,
                                               String tsForecastHorizon)  throws REXPMismatchException, REngineException {
        String baggedETSForecastCall = "forecastSTLWrapper(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "frequencyts = " + frequencyts + ", " + "horizon = " + tsForecastHorizon + ")";

        return Common.evaluateRforecast(this.rconnection, baggedETSForecastCall, elementName);
    }

    private void trainNNModel(String elementName, String index, String frequencyts)
            throws REXPMismatchException, REngineException {
        String NNModelTrainCall = "trainNNModel(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "frequencyts = " + frequencyts + ")";

        Common.evaluateR(this.rconnection, NNModelTrainCall);
    }

    private ForecastDTO forecastNNModel(String elementName, String index, String frequencyts,
                                         String tsForecastHorizon)  throws REXPMismatchException, REngineException {
        String NNForecastCall = "forecastNNWrapper(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "frequencyts = " + frequencyts + ", " + "horizon = " + tsForecastHorizon + ")";

        return Common.evaluateRforecast(this.rconnection, NNForecastCall, elementName);
    }

    private void trainHybridModel(String elementName, String index, String cvHorizon, String frequencyts)
            throws REXPMismatchException, REngineException {
        String hybridTrainCall = "trainHybridModel(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "cvHorizon = " + cvHorizon + ", frequencyts = " + frequencyts + ")";

        Common.evaluateR(this.rconnection, hybridTrainCall);
    }

    private ForecastDTO forecastHybridModel(String elementName, String index, String frequencyts,
                                        String tsForecastHorizon)  throws REXPMismatchException, REngineException {
        String hybridForecastCall = "forecastHybridWrapper(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "frequencyts = " + frequencyts + ", " + "horizon = " + tsForecastHorizon + ")";

        return Common.evaluateRforecast(this.rconnection, hybridForecastCall, elementName);
    }

    private void trainProphetModel(String elementName, String index)
            throws REXPMismatchException, REngineException {
        String prophetModelTrainCall = "trainProphetModel(name = \"" + elementName + "\", " + "index = \"" + index + "\")";

        Common.evaluateR(this.rconnection, prophetModelTrainCall);
    }

    private ForecastDTO forecastProphetModel(String elementName, String index, String tsForecastHorizon)
            throws REXPMismatchException, REngineException {
        String prophetForecastCall = "forecastProphetWrapper(name = \"" + elementName + "\", " + "index = \"" + index + "\", " +
                "horizon = " + tsForecastHorizon + ")";

        return Common.evaluateRforecast(this.rconnection, prophetForecastCall, elementName);
    }
}