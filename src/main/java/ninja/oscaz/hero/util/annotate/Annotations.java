package ninja.oscaz.hero.util.annotate;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ninja.oscaz.hero.util.except.UtilityInstantiationException;

public final class Annotations {

    public static Optional<Annotation> findSingleSecondary(AnnotatedElement element, Class<? extends Annotation> annotation) {
        List<Annotation> annotations = Annotations.findAllSecondary(element, annotation);

        if (annotations.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(annotations.get(0));
    }

    public static List<Annotation> findAllSecondary(AnnotatedElement element, Class<? extends Annotation> annotation) {
        List<Annotation> annotations = new ArrayList<>();

        for (Annotation baseAnnotation : element.getAnnotations()) {
            if (baseAnnotation.annotationType().isAnnotationPresent(annotation)) {
                annotations.add(baseAnnotation);
            }
        }

        return annotations;
    }

    public static <T extends Annotation> Optional<T> findSecondaryValue(AnnotatedElement element, Class<T> annotation) {
        for (Annotation baseAnnotation : element.getAnnotations()) {
            if (baseAnnotation.annotationType().isAnnotationPresent(annotation)) {
                return Optional.of(baseAnnotation.annotationType().getAnnotation(annotation));
            }
        }

        return Optional.empty();
    }

    private Annotations() {
        throw new UtilityInstantiationException();
    }

}
