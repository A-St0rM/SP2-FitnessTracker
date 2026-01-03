# Start with Amazon Corretto 17 Alpine base image
FROM amazoncorretto:17-alpine

# Install curl on Alpine
RUN apk update && apk add --no-cache curl

# Copy the fat JAR explicitly
COPY target/SP2-FitnessTracker-1.0-SNAPSHOT-shaded.jar /app.jar

# Expose the port your app runs on
EXPOSE 7007

# Command to run your app
CMD ["java", "-jar", "/app.jar"]