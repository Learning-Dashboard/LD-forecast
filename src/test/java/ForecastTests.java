import Forecast.MongoDB_RForecast;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.testng.Assert;
import org.testng.annotations.*;
import Forecast.Common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ForecastTests {

    // Modify the parameters if needed, depending on your deployment
    private String mongo_host = "mongodb";
    private int mongo_port = 27017;
    private String mongo_database = "mongo";
    private String Rscriptslocation = "/home/ruser/TimeSeriesFunctions_GPL_0.6.R";
    private MongoDB_RForecast rForecast;

    // Personalize with the parameters you want to test
    private String element_name = "deviation_effort_estimation_simple";
    private String index = "metrics.pes11a";
    private String tsFrequency = "7";
    private String tsForecastHorizon = "20";

    // Replace with the location and names of the directories (in local machine)
    private String models_dir = "forecastModels";
    private String for_cache_dir = "forecastsCache";
    private String baseDir = "";

    @BeforeClass
    public void connectR() throws REXPMismatchException, REngineException {
        rForecast = new MongoDB_RForecast(mongo_host, mongo_port, mongo_database, Rscriptslocation);
    }

    @Test
    public void test_training_arima() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_model_file = baseDir + "/" + models_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.ARIMA;
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.ARIMA;
        Files.deleteIfExists(new File(clean_model_file).toPath());
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.trainForecastModel(element_name, index, tsFrequency, Common.ForecastTechnique.ARIMA);
        Assert.assertTrue(Files.exists(new File(clean_model_file).toPath()));
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_forecasting_arima() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.ARIMA;
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.forecast(element_name, index, tsFrequency, tsForecastHorizon, Common.ForecastTechnique.ARIMA);
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_training_arima_force_seasonality() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_model_file = baseDir + "/" + models_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.ARIMA_FORCE_SEASONALITY;
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.ARIMA_FORCE_SEASONALITY;
        Files.deleteIfExists(new File(clean_model_file).toPath());
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.trainForecastModel(element_name, index, tsFrequency, Common.ForecastTechnique.ARIMA_FORCE_SEASONALITY);
        Assert.assertTrue(Files.exists(new File(clean_model_file).toPath()));
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_forecasting_arima_force_seasonality() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.ARIMA_FORCE_SEASONALITY;
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.forecast(element_name, index, tsFrequency, tsForecastHorizon, Common.ForecastTechnique.ARIMA_FORCE_SEASONALITY);
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_training_theta() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_model_file = baseDir + "/" + models_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.THETA;
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.THETA;
        Files.deleteIfExists(new File(clean_model_file).toPath());
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.trainForecastModel(element_name, index, tsFrequency, Common.ForecastTechnique.THETA);
        Assert.assertTrue(Files.exists(new File(clean_model_file).toPath()));
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_forecasting_theta() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.THETA;
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.forecast(element_name, index, tsFrequency, tsForecastHorizon, Common.ForecastTechnique.THETA);
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_training_ets() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_model_file = baseDir + "/" + models_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.ETS;
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.ETS;
        Files.deleteIfExists(new File(clean_model_file).toPath());
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.trainForecastModel(element_name, index, tsFrequency, Common.ForecastTechnique.ETS);
        Assert.assertTrue(Files.exists(new File(clean_model_file).toPath()));
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_forecasting_ets() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.ETS;
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.forecast(element_name, index, tsFrequency, tsForecastHorizon, Common.ForecastTechnique.ETS);
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_training_etsdamped() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_model_file = baseDir + "/" + models_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.ETSDAMPED;
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.ETSDAMPED;
        Files.deleteIfExists(new File(clean_model_file).toPath());
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.trainForecastModel(element_name, index, tsFrequency, Common.ForecastTechnique.ETSDAMPED);
        Assert.assertTrue(Files.exists(new File(clean_model_file).toPath()));
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_forecasting_etsdamped() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.ETSDAMPED;
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.forecast(element_name, index, tsFrequency, tsForecastHorizon, Common.ForecastTechnique.ETSDAMPED);
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_training_baggedets() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_model_file = baseDir + "/" + models_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.BAGGEDETS;
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.BAGGEDETS;
        Files.deleteIfExists(new File(clean_model_file).toPath());
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.trainForecastModel(element_name, index, tsFrequency, Common.ForecastTechnique.BAGGEDETS);
        Assert.assertTrue(Files.exists(new File(clean_model_file).toPath()));
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_forecasting_baggedets() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.BAGGEDETS;
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.forecast(element_name, index, tsFrequency, tsForecastHorizon, Common.ForecastTechnique.BAGGEDETS);
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_training_stl() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_model_file = baseDir + "/" + models_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.STL;
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.STL;
        Files.deleteIfExists(new File(clean_model_file).toPath());
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.trainForecastModel(element_name, index, tsFrequency, Common.ForecastTechnique.STL);
        Assert.assertTrue(Files.exists(new File(clean_model_file).toPath()));
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_forecasting_stl() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.STL;
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.forecast(element_name, index, tsFrequency, tsForecastHorizon, Common.ForecastTechnique.STL);
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_training_NN() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_model_file = baseDir + "/" + models_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.NN;
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.NN;
        Files.deleteIfExists(new File(clean_model_file).toPath());
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.trainForecastModel(element_name, index, tsFrequency, Common.ForecastTechnique.NN);
        Assert.assertTrue(Files.exists(new File(clean_model_file).toPath()));
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_forecasting_NN() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.NN;
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.forecast(element_name, index, tsFrequency, tsForecastHorizon, Common.ForecastTechnique.NN);
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_training_Hybrid() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_model_file = baseDir + "/" + models_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.HYBRID;
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.HYBRID;
        Files.deleteIfExists(new File(clean_model_file).toPath());
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.trainForecastModel(element_name, index, tsFrequency, Common.ForecastTechnique.HYBRID);
        Assert.assertTrue(Files.exists(new File(clean_model_file).toPath()));
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_forecasting_Hybrid() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.HYBRID;
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.forecast(element_name, index, tsFrequency, tsForecastHorizon, Common.ForecastTechnique.HYBRID);
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_training_Prophet() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_model_file = baseDir + "/" + models_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.PROPHET;
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.PROPHET;
        Files.deleteIfExists(new File(clean_model_file).toPath());
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.trainForecastModel(element_name, index, tsFrequency, Common.ForecastTechnique.PROPHET);
        Assert.assertTrue(Files.exists(new File(clean_model_file).toPath()));
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @Test
    public void test_forecasting_Prophet() throws REXPMismatchException, REngineException, IOException {
        String clean_element_name = element_name.replaceAll("[^a-zA-Z\\d ]", "");
        String clean_forecast_file = baseDir + "/" + for_cache_dir + "/" + clean_element_name + "_" + index + "_" + Common.ForecastTechnique.PROPHET;
        Files.deleteIfExists(new File(clean_forecast_file).toPath());

        rForecast.forecast(element_name, index, tsFrequency, tsForecastHorizon, Common.ForecastTechnique.PROPHET);
        Assert.assertTrue(Files.exists(new File(clean_forecast_file).toPath()));
    }

    @AfterClass
    public void close() {
        rForecast.close();
    }

}
