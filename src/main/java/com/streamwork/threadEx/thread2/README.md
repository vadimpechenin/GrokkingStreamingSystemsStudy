## Часть II — синхронизация

# 1. Yield

Метод Thread.yield() загадочный и редко используемый. Существует много вариаций его описания в интернете. 
Вплоть до того, что некоторые пишут про какую-то очередь потоков, в которой поток переместится вниз с учётом их приоритетов. 
Кто-то пишет, что поток изменит статус с running на runnable (хотя разделения на эти статусы нет, и Java их не различает). Но на самом деле всё куда неизвестнее и в каком-то смысле проще.

На тему документации метода yield есть баг "JDK-6416721 : (spec thread) Fix Thread.yield() javadoc". Если прочитать его, 
то понятно, что на самом деле метод yield лишь передаёт некоторую рекомендацию планировщику потоков Java, 
что данному потоку можно дать меньше времени исполнения. Но что будет на самом деле, услышит ли планировщик рекомендацию 
и что вообще он будет делать — зависит от реализации JVM и операционной системы. А может и ещё от каких-то других факторов. 
Вся путаница сложилась, скорее всего, из-за переосмысления многопоточности в процессе развития языка Java.

# 2. Sleep - Засыпание потока HelloWorldApp

Поток в процессе своего выполнения может засыпать. Это самой простой тип взаимодействия с другими потоками.
В операционной системе, на которой установлена виртуальная Java машина, где выполняется Java код, есть свой планировщик потоков, называемый Thread Scheduler. 
Именно он решает, какой поток когда запускать.

Программист не может взаимодействовать с этим планировщиком напрямую из Java кода, 
но он может через JVM попросить планировщик на какое-то время поставить поток на паузу, "усыпить" его. Подробнее можно прочитать в статьях "Thread.sleep()" и "How Multithreading works". Более того, можно узнать, как устроены потоки в Windows OS: "Internals of Windows Thread".

"C:\Program Files\Java\jdk-17.0.10\bin\javac.exe" HelloWorldApp.java

start java HelloWorldApp

# 3. Прерывание потока или Thread.interrupt - HelloWorldAppInterrupt
Вы наверно заметили, что мы везде обрабатываем InterruptedException? Давайте поймём, зачем.

Всё дело в том, что пока поток ожидает во сне, кто-то может захотеть прервать это ожидание. 
На этот случай мы обрабатываем такое исключение. Сделано это было после того, как метод Thread.stop объявили Deprecated, 
т.е. устаревшим и нежелательным к использованию. Причиной тому было то, что при вызове метода stop поток просто "убивался", 
что было очень непредсказуемо. Мы не могли знать, когда поток будет остановлен, не могли гарантировать консистентность данных. 

Представте, что вы пишете данные в файл и тут поток уничтожают. Поэтому, решили, что логичнее будет поток не убивать, 
а информировать его о том, что ему следует прерваться. Как на это реагировать — дело самого потока. 
Более подробно можно прочитать у Oracle в "Why is Thread.stop deprecated?".

В примере interrupt1 мы не будем ждать 60 секунд, а сразу напечатаем 'Interrupted'. Всё потому, что мы вызвали у потока метод interrupt.
Данный метод выставляет "internal flag called interrupt status". То есть у каждого потока есть внутренний флаг, недоступный напрямую. 
Но у нас есть native методы для взаимодействия с этим флагом.

Но это не единственный способ. Поток может быть в процессе выполнения, не ждать чего-то, а просто выполнять действия. 
Но может предусмотреть, что его захотят завершить в определённый момент его работы - см. interrupt2.
В примере выше видно, что цикл while будет выполняться до тех пор, пока поток не прервут снаружи.
Про флаг isInterrupted важно знать то, что если мы поймали InterruptedException, флаг isInterrupted сбрасывается, 
и тогда isInterrupted будет возвращать false. Есть также статический метод у класса Thread, который относится только 
к текущему потоку — Thread.interrupted(), но данный метод сбрасывает значение флага на false!

# 4. Join — Ожидание завершения другого потока - HelloWorldAppJoin 
Самым простым типом ожидания является ожидание завершения другого потока.
В данном примере новый поток будет спать 5 секунд. В то же время, главный поток main будет ждать, пока спящий поток 
не проснётся и не завершит свою работу.
Метод join довольно прост, потому что является просто методом с java кодом, который выполняет wait, пока поток, на котором он вызван, живёт. 
Как только поток умирает (при завершении), ожидание прерывается. Вот и вся магия метода join. Поэтому, перейдём к самому интересному.

# 5. Понятие Монитор - HelloWorldSynch 
В многопоточности есть такое понятие, как Monitor. Вообще, слово монитор с латинского переводится как "надзиратель" или "надсмотрщик".

В рамках данной статьи попытаемся вспомнить суть, а кто хочет — за подробностями прошу погрузиться в материал из ссылок. 
Начнём наш путь со спецификации языка Java, то есть с JLS: "17.1. Synchronization".
Получается, что для целей синхронизации между потоками Java использует некий механизм, который называется "Монитор". С каждым объектом ассоциирован некоторый монитор, а потоки могут его заблокировать "lock" или разблокировать "unlock".

Далее, найдём на сайте Oracle обучающий tutorial: "Intrinsic Locks and Synchronization".

В данном туториале говорится, что синхронизация в Java построена вокруг внутренней сущности (internal entity), известной как intrinsic lock или monitor lock. Часто такой лок называют просто "монитор".

Также мы опять видим, что каждый объект в Java имеет ассоциированный с ним intrinsic lock. Почитать можно "Java - Intrinsic Locks and Synchronization".

Далее важно понять, каким образом объект в Java может быть связан с монитором. У каждого объекта в Java есть заголовок (header) — своего рода внутренние метаданные, которые недоступны программисту из кода, но которые нужны виртуальной машине, чтобы работать с объектами правильно.

В состав заголовка объекта входит MarkWord.

Тут очень пригодится статья с хабра: "А как же всё-таки работает многопоточность? Часть I: синхронизация".

К этой статье стоит прибавить описание из Summary блока таска с багтекера JDK: "JDK-8183909". Можно тоже самое прочитать в "JEP-8183909".

Итак, в Java с объектом ассоциирован монитор и поток получается заблокировать этот поток или ещё говорят "получить лок".

Итак, при помощи ключевого слова synchronized текущий поток (в котором выполняются эти строки кода) пытается использовать монитор, ассоциированный с объектом object и "получить лок" или "захватить монитор" (второй вариант даже предпочтетельнее). Если за монитор нет соперничества (т.е. никто больше не хочет выполнить synchronized по такому же объекту), Java может попытаться выполнить оптимизацию, называемую "biased locking". В заголовке объекта в Mark Word выставится соответствующий тэг и запись о том, к какому потоку привязан монитор. Это позволяет сократить накладные расходы при захватывании монитора.

Если монитор уже ранее был привязан к другому потоку, тогда такой блокировки недостаточно. JVM переключается на следующий тип блокировки — basic locking. Она использует compare-and-swap (CAS) операции. При этом в заголовке в Mark Word уже хранится не сам Mark Word, а ссылка на его хранение + изменяется тэг, чтобы JVM поняла, что у нас используется базовая блокировка.

Если же возникает соперничество (contention) за монитор нескольких потоков (один захватил монитор, а второй ждёт освобождение монитора), тогда тэг в Mark Word меняется, и в Mark Word начинает храниться ссылка уже на монитор как объект — некоторую внутреннюю сущность JVM. Как сказано в JEP, в таком случае требуется место в Native Heap области памяти на хранение этой сущности. Ссылка на место хранения этой внутренней сущности и будет находиться в Mark Word объекта.

Таким образом, как мы видим, монитор — это действительно механизм обеспечения синхронизации доступа нескольких потоков к общим ресурсам. Существует несколько реализаций этого механизма, между которыми переключается JVM. Поэтому для простоты, говоря про монитор, мы говорим на самом деле про локи.

# 6. Synchronized и ожидание по локу - LocWait
С понятием монитора, как мы ранее видели, тесно связано понятие "блок синхронизации" (или как ещё называют — критическая секция).

В LocWait главный поток сначала отправляет задачу task в новый поток, а потом сразу же "захватывает" лок и выполняет с ним долгую операцию (8 секунд). Всё это время task не может для своего выполнения зайти в блок synchronized, т.к. лок уже занят.

Если поток не может получить лок, он будет ждать этого у монитора. Как только получит — продолжит выполнение. Когда поток выходит из-под монитора, он освобождает лок.

Как видно, статус в JVisualVM называется "Monitor", потому что поток заблокирован и не может занять монитор. В коде тоже можно узнать состояние потока, но название этого состояния не совпадает с терминами JVisualVM, хотя они и схожи. В данном случае th1.getState() в цикле for будет возвращать BLOCKED, т.к. пока выполняется цикл, монитор lock занят main потоком, а поток th1 заблокирован и не может продолжать работу, пока лок не вернут.

# 7. Wait и ожидание по монитору. Методы notify и notifyAll - WaitNotifyAndNogiryAll
У Thread есть ещё один метод ожидания, который при этом связан с монитором. В отличие от sleep и join, его нельзя просто так вызвать. И зовут его wait.

Выполняется метод wait на объекте, на мониторе которого мы хотим выполнить ожидание. 

Чтобы разобраться, как это работает, следует вспомнить, что методы wait и notify относятся к java.lang.Object. Кажется странным, что методы, относящиеся к потокам, находятся в классе Object. Но тут то и кроется ответ.

Как мы помним, каждый объект в Java имеет заголовок. В заголовке содержится различная служебная информация, в том числе и информация о мониторе — данные о состоянии блокировки. И как мы помним, каждый объект (т.е. каждый instance) имеет ассоциацию с внутренней сущностью JVM, называемой локом (intrinsic lock), который так же называют монитором.

В примере выше в задаче task описано, что мы входим в блок синхронизации по монитору, ассоциированному с lock. Если удаётся получить лок по этому монитору, то выполняется wait. Поток, выполняющий этот task, будет освобождать монитор lock, но становиться в очередь потоков, ожидающих уведомления по монитору lock. Эта очередь потоков называется WAIT-SET, что более правильно отражает суть. Это скорее набор, а не очередь.

Поток main создаёт новый поток с задачей task, запускает его и ждёт 3 секунды. Это позволяет с большой долей вероятности новому потоку захватить лок прежде, чем поток main, и встать в очередь по монитору. После чего поток main сам входит в блок синхронизации по lock и выполняет уведомление потока по монитору. После того, как уведомление отправлено, поток main освобождает монитор lock, а новый поток (который ранее ждал) дождавшись освобождения монитора lock, продолжает выполнение.

Существует возможность отправить уведомление только одному из потоков (notify) или сразу всем потокам из очереди (notifyAll).
Подробнее можно прочитать в "Difference between notify() and notifyAll() in Java".

Важно отметить, что порядок уведомления зависит от реализации JVM. Подробнее можно прочитать в "How to solve starvation with notify and notifyall?".

Синхронизация может выполняться без указания объекта. Это можно сделать, когда синхронизирован не отдельный участок кода, а целый метод.

Например, для статических методов локом будет объект класса (полученный через .class).

Если метод не статический, то синхронизация будет выполняться по текущему instance, то есть по this.

Кстати, ранее мы говорили, что при помощи метода getState можно получить статус потока. Так вот поток, который становится в очередь по монитору, статус будет WAITING или TIMED_WAITING, если в методе wait было указано ограничение по времени ожидания.

# 8. Жизненный цикл потока
Как мы видели, поток в процессе жизни меняет свой статус. По сути эти изменения и являются жизненным циклом потока.

Когда поток только создан, то он имеет статус NEW. В таком положении он ещё не запущен и планировщик потоков Java (Thread Scheduler) ещё не знает ничего о новом потоке.

Для того, чтобы о потоке узнал планировщик потоков, необходимо вызвать метод thread.start(). Тогда поток перейдёт в состояние RUNNABLE. В интернете есть много неправильных схем, где разделяют состояния Runnable и Running. Но это ошибка, т.к. Java не отличает статус "готов к работе" и "работает (выполняется)".

Когда поток жив, но не активен (не Runnable), он находится в одном из двух состояний:
BLOCKED — ожидает захода в защищённую (protected) секцию, т.е. в synchonized блок.
WAITING — ожидает другой поток по условию. Если условие выполняется, планировщик потоков запускает поток.
Если поток ожидает по времени, он находится в статусе TIMED_WAITING.

Если поток больше не выполняется (завершился успешно или с exception), он переходит в статус TERMINATED.

Чтобы узнать состояние потока (его state), используется метод getState.

У потоков также есть метод isAlive, который возвращает true, если поток не Terminated.

# 9. LockSupport и парковка потоков - LockSupport и LockSupport2
Начиная с Java 1.6 появился интересный механизм, называемый LockSupport.
Данный класс ассоциирует с каждым потоком, который его использует, "permit" или разрешение.

Вызов метода park возвращается немедленно, если permit доступен, занимая этот самый permit в процессе вызова. Иначе он блокируется.

Вызов метода unpark делает permit доступным, если он ещё недоступен.

Permit есть всего 1.

В Java API для LockSupport ссылаются на некий Semaphore.

В LockSupport код будет вечно ждать, потому что в семафоре сейчас 0 permit. А когда в коде вызывается acquire (т.е. запросить разрешение), то поток ожидает, пока разрешение не получит.

Так как мы ждём, то обязаны обработать InterruptedException.

Интересно, что семафор реализует отдельное состояние потока. Если мы посмотрим в JVisualVM, то увидим, что у нас состояние не Wait, а Park.

Как видим, в него можно попасть только тремя способами. 2 способа — это wait и join. А третий — это LockSupport.

Локи в Java построены так же на LockSupport и представляют более высокоуровневые инструменты. Давайте попробуем воспользоваться таковым.

Посмотрим, например, на ReentrantLock в LockSupport2

Как и в прошлых примерах, тут всё просто. lock ожидает, пока кто-то освободит ресурс. Если посмотреть в JVisualVM, мы увидим, что новый поток будет запаркован, пока main поток не отдаст ему лок.
