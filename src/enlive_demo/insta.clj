;; emit*

;; snippet*

(ns enlive-demo.insta
  (:use [net.cgrand.enlive-html]
        [clojure.pprint :only [pprint]])
  (:require [compojure.core :as c]
            [ring.adapter.jetty :as jetty]))

(def p (comp println pprint))

;; html-content

(def h (html-resource "templates/heroes.html"))

;; selectors
(p (select h [:h1]))

(count (select h [:ul :li]))

(p (select h [:ul#list :li :h2.name]))

(select h [:div :> :a])

(p (select h [:ul [:li (nth-child 1)] :h2]))

(p (select h [:ul [:li (attr? :data-focus)] :h2]))

(p (select h [:ul [:li (attr= :data-focus "true")] :h2]))

(p (select h [[:li (but (attr? :data-focus))] :h2]))

(p (select h [:li #{[:p] [:a]}]))

(select h #{[:h1] [:h2]})

(select h [(pred #(= (:tag %) :a))])

;; find all twitter URLS
(-> h (select [:ul :a]) first :attrs :href)

(:href (:attrs (first (select h [:ul :a]))))







(map #(get-in % [:attrs :href]) (select h [:ul :a]))






(map (comp :href :attrs) (select h [:ul :a]))






(select h [(text-pred #(re-find #"@" %))])

;; find all element with twitter handle in the content


;; ==========================================
;; Transformations
(sniptest "<span>foo</span>"
          [:*] (wrap :strong))

(def n2 (html-snippet "<div><span>hello</span></div>"))

(at n2
    [:span] (wrap :strong))

(at n2
    [:span] (wrap :strong)
    [:div] (append {:tag :br :content nil :attrs nil}))

(apply str
 (emit*
   (at n2
      [:span] (wrap :strong)
      [:div] (append {:tag :br :content nil :attrs nil}))))

(declare t)

(c/defroutes app
  (c/GET "/" [] (t)))

(defonce server (jetty/run-jetty #'app {:port 8081 :join? false}))

(.stop server)
(.start server)

(defmacro tt [ & forms]
  `(defn t []
     (emit* (at h ~@forms))))

(tt)

(tt [:a] (wrap :strong))

(tt [:li :a] (wrap :strong))

(tt [[:li (attr= :data-focus "true")]] (set-attr :style "background-color:yellow"))

(emit*
 (at
   (html-snippet "<div>hello</div>")
   [:div] (wrap :strong)))



(sniptest "<ul><li><a href='#'>link</a></li></ul>"
          [:li] (clone-for [link ["one" "two"]]
                           [:a] (do->
                                 (content link)
                                 (set-attr :href (str "#" link)))))


;; Snippets

(defsnippet post-snippet "blog_template.html" [:.post]
  [post-title author body]
  [:h2] (content post-title)
  [:.author] (content author)
  [:.body] (content body))

;; Templates

(deftemplate blog-template "blog_template.html"
  [title posts]
  [:h1] (content title)
  [:#posts] (content (map post-snippet posts)))

(deftemplate "template.html"
  [data]
  [:div] (substitute {:tag :p :attrs nil :content nil})
  [:p] (content "hello"))

(sniptest "<div>foo</div>"
          [:div] (substitute {:tag :p :attrs nil :content nil})
          [:p] (content "hello"))

(sniptest "<div>foo</div>"
          [:div] (substitute (html [:p]))
          [:p] (content "hello"))

(html [:p])