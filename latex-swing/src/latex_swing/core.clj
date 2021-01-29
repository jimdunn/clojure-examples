(ns latex-swing.core
  (:import [org.apache.batik.swing JSVGCanvas]
           [org.apache.batik.dom GenericDOMImplementation]
           [org.apache.batik.svggen SVGGeneratorContext]
           [org.apache.batik.svggen SVGGraphics2D]
           [java.awt Insets Dimension Color]
           [javax.swing JLabel JFrame JPanel]
           [org.scilab.forge.jlatexmath TeXFormula TeXConstants TeXIcon]))

;; create a tmp file for the graphics
(defn svg-graphics->tmp-file [g2 file-name]
  (let [tmp (java.io.File/createTempFile file-name "svg")
        svgs (java.io.FileOutputStream. tmp)
        out (java.io.OutputStreamWriter. svgs "UTF-8")
        use-css? true]
    (.stream g2 out use-css?)
    (.flush svgs)
    (.close svgs)
    (.deleteOnExit tmp)
    tmp))

;; from latex string, generate svg graphics, convert to canvas object we can add to a jpanel
(defn latex->svg-canvas [latex]
  (let [fonts-as-shapes? true
        dom-impl (GenericDOMImplementation/getDOMImplementation)
        svg-ns "http://www.w3.org/2000/svg"
        document (.createDocument dom-impl svg-ns "svg" nil)
        context (SVGGeneratorContext/createDefault document)
        svg-graphics (SVGGraphics2D. context fonts-as-shapes?)
        insets (Insets. 5 5 5 5)
        formula (TeXFormula. latex)
        icon (doto (.createTeXIcon formula TeXConstants/STYLE_DISPLAY 20) (.setInsets insets))
        w (.getIconWidth icon) h (.getIconHeight icon)
        label (doto (JLabel.) (.setForeground (Color. 0 0 0)))]
    (doto svg-graphics
      (.setSVGCanvasSize (Dimension. w h))
      (.setColor Color/white)
      (.fillRect 0 0 w h))
    (.paintIcon icon label svg-graphics 0 0)
    (doto (JSVGCanvas.)
      (.setURI (-> (svg-graphics->tmp-file svg-graphics "latex-tmp") (.toURL) (.toString))))))

;; just playing around with ways to generate latex more easily
(defn frac [num denom] (format "\\frac{%s}{%s}" num denom))
(defn sin [x] (format "\\sin(%s)" x))
(defn sqrt [x] (format "\\sqrt{%s}" x))
(defn integral [x] (format "\\int{%s}" x))
(defn mult [x y] (format "%s %s" x y))
(defn add [x y] (format "%s + %s" x y))

(defn run []
  (let [frame (JFrame.)
        latex (integral (mult (frac (sin "\\theta") (sqrt (add 1 "\\theta^2")) ) "d\\theta"))
        svg-canvas (latex->svg-canvas latex)
        panel (doto (JPanel.) (.add svg-canvas))]
    (doto (JFrame.)
      (.setContentPane panel)
      (.setSize 200 105)
      (.setVisible true))))
