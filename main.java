import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Singleton para administrar la configuración de actividades y rutinas
class ActivityManager {
    private static ActivityManager instance;
    private List<Activity> activities;
    private List<UserProfile> userProfiles;
    private List<Routine> routines;

    private ActivityManager() {
        activities = new ArrayList<>();
        userProfiles = new ArrayList<>();
        routines = new ArrayList<>();
    }

    public static synchronized ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
        System.out.println("Activity added: " + activity);
    }

    public void addUserProfile(UserProfile userProfile) {
        userProfiles.add(userProfile);
        System.out.println("User Profile added: " + userProfile);
    }

    public List<Activity> getActivityHistory() {
        return activities;
    }

    public List<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public List<String> getRoutineRecommendations(UserProfile userProfile) {
        List<String> recommendations = new ArrayList<>();
        double bmi = userProfile.calculateBMI();

        if (bmi < 18.5) {
            recommendations.add("Ganar masa muscular con pesas.");
            recommendations.add("Rutina de fuerza 3 veces por semana.");
            recommendations.add("Dieta rica en proteínas.");
        } else if (bmi >= 18.5 && bmi < 24.9) {
            recommendations.add("Mantener con ejercicios de cardio.");
            recommendations.add("Rutina de resistencia 4 veces por semana.");
            recommendations.add("Dieta balanceada con control calórico.");
        } else {
            recommendations.add("Pérdida de peso con ejercicios aeróbicos.");
            recommendations.add("Rutina HIIT 5 veces por semana.");
            recommendations.add("Dieta baja en calorías y grasas.");
        }

        return recommendations;
    }

    public void createRoutine(UserProfile userProfile, int daysAvailable, int hoursPerDay) {
        Routine routine = new RoutineBuilder()
                .setUserProfile(userProfile)
                .setDaysAvailable(daysAvailable)
                .setHoursPerDay(hoursPerDay)
                .build();

        routines.add(routine);
        System.out.println("Routine created: " + routine);
    }

    public List<Routine> getRoutines() {
        return routines;
    }

    public void markTaskAsCompleted(RoutineTask task) {
        task.markAsCompleted();
    }
}

// Clase UserProfile para almacenar los datos del usuario
class UserProfile {
    private String name;
    private double weight;
    private double height;
    private int age;
    private String availability;

    public UserProfile(String name, double weight, double height, int age, String availability) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.availability = availability;
    }

    public double calculateBMI() {
        return weight / (height * height);
    }

    public String getAvailability() {
        return availability;
    }

    @Override
    public String toString() {
        return "Usuario: " + name + ", Peso: " + weight + "kg, Estatura: " + height + "m, Edad: " + age + ", Disponibilidad: " + availability;
    }
}

// Abstract Factory para crear actividades
abstract class Activity {
    public abstract String getType();
}

class Cardio extends Activity {
    @Override
    public String getType() {
        return "Cardio";
    }
}

class StrengthTraining extends Activity {
    @Override
    public String getType() {
        return "Entrenamiento de Fuerza";
    }
}

class Yoga extends Activity {
    @Override
    public String getType() {
        return "Yoga";
    }
}

interface ActivityFactory {
    Activity createActivity();
}

class CardioFactory implements ActivityFactory {
    @Override
    public Activity createActivity() {
        return new Cardio();
    }
}

class StrengthTrainingFactory implements ActivityFactory {
    @Override
    public Activity createActivity() {
        return new StrengthTraining();
    }
}

class YogaFactory implements ActivityFactory {
    @Override
    public Activity createActivity() {
        return new Yoga();
    }
}

// Clase Routine para gestionar las rutinas
class Routine {
    private UserProfile userProfile;
    private int daysAvailable;
    private int hoursPerDay;
    private List<RoutineTask> tasks;

    private Routine(RoutineBuilder builder) {
        this.userProfile = builder.userProfile;
        this.daysAvailable = builder.daysAvailable;
        this.hoursPerDay = builder.hoursPerDay;
        this.tasks = builder.tasks;
    }

    public List<RoutineTask> getTasks() {
        return tasks;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    @Override
    public String toString() {
        return "Rutina para " + userProfile.toString() + ", Días disponibles: " + daysAvailable + ", Horas por día: " + hoursPerDay;
    }
}

// Clase RoutineTask para gestionar cada tarea en la rutina
class RoutineTask {
    private String day;
    private int hours;
    private String activityType;
    private boolean completed;

    public RoutineTask(String day, int hours, String activityType, boolean completed) {
        this.day = day;
        this.hours = hours;
        this.activityType = activityType;
        this.completed = completed;
    }

    public void markAsCompleted() {
        this.completed = true;
    }

    @Override
    public String toString() {
        return day + ": " + hours + " horas de " + activityType + " - " + (completed ? "Completado" : "No completado");
    }
}

// Builder para la clase Routine
class RoutineBuilder {
    UserProfile userProfile;
    int daysAvailable;
    int hoursPerDay;
    List<RoutineTask> tasks = new ArrayList<>();

    public RoutineBuilder setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public RoutineBuilder setDaysAvailable(int daysAvailable) {
        this.daysAvailable = daysAvailable;
        return this;
    }

    public RoutineBuilder setHoursPerDay(int hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
        return this;
    }

    public Routine build() {
        generateRoutineTasks();
        return new Routine(this);
    }

    private void generateRoutineTasks() {
        String[] daysOfWeek = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        Random rand = new Random();

        for (int i = 0; i < daysAvailable; i++) {
            String day = daysOfWeek[i % 7];
            // Rutina aleatoria: Cardio, Fuerza o Yoga
            String activityType;
            int randomNum = rand.nextInt(3);
            switch(randomNum) {
                case 0:
                    activityType = "Cardio";
                    break;
                case 1:
                    activityType = "Fuerza";
                    break;
                default:
                    activityType = "Yoga";
            }
            tasks.add(new RoutineTask(day, hoursPerDay, activityType, false));
        }
    }
}

// Clase principal
public class main {
    public static void main(String[] args) {
        ActivityManager manager = ActivityManager.getInstance();

        // Crear perfil del usuario
        UserProfile userProfile = new UserProfile("Carlos Perez", 70.5, 1.75, 28, "Tres días a la semana");
        manager.addUserProfile(userProfile);

        // Añadir actividades
        ActivityFactory cardioFactory = new CardioFactory();
        manager.addActivity(cardioFactory.createActivity());

        ActivityFactory strengthFactory = new StrengthTrainingFactory();
        manager.addActivity(strengthFactory.createActivity());

        ActivityFactory yogaFactory = new YogaFactory();
        manager.addActivity(yogaFactory.createActivity());

        // Crear rutina en función de la disponibilidad
        manager.createRoutine(userProfile, 3, 2);

        // Mostrar recomendaciones de rutina
        List<String> recommendations = manager.getRoutineRecommendations(userProfile);
        System.out.println("Recomendaciones:");
        for (String rec : recommendations) {
            System.out.println(rec);
        }

        // Mostrar rutina generada
        List<Routine> routines = manager.getRoutines();
        for (Routine routine : routines) {
            System.out.println(routine);
            for (RoutineTask task : routine.getTasks()) {
                System.out.println(task);
            }
        }
    }
}
