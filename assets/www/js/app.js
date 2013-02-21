"use strict";

// Creating the application namespace
var directory = {
    models: {},
    views: {},
    utils: {},
    dao: {}
};

// -------------------------------------------------- Utilities ---------------------------------------------------- //

// The Template Loader. Used to asynchronously load templates located in separate .html files
directory.utils.templateLoader = {

    templates: {},

    load: function(names, callback) {

        var deferreds = [],
            self = this;

        $.each(names, function(index, name) {
            deferreds.push($.get('tpl/' + name + '.html', function(data) {
                self.templates[name] = data;
            }));
        });

        $.when.apply(null, deferreds).done(callback);
    },

    // Get template by name from hash of preloaded templates
    get: function(name) {
        return this.templates[name];
    }

};

// The Employee Data Access Object (DAO). Encapsulates logic (in this case SQL statements) to access employee data.
directory.dao.EmployeeDAO = function(db) {
    this.db = db;
};

_.extend(directory.dao.EmployeeDAO.prototype, {

    findByName: function(key, callback) {
        this.db.transaction(
            function(tx) {

                var sql = "SELECT e.id, e.name, e.title " +
                    "FROM employee e " +
                    "WHERE e.name LIKE ? " +
                    "GROUP BY e.id ORDER BY e.name";

                tx.executeSql(sql, ['%' + key + '%'], function(tx, results) {
                    var len = results.rows.length,
                        employees = [],
                        i = 0;
                    for (; i < len; i = i + 1) {
                        employees[i] = results.rows.item(i);
                    }
                    callback(employees);
                });
            },
            function(tx, error) {
                alert("Transaction Error: " + error);
            }
        );
    },

    findById: function(id, callback) {
        this.db.transaction(
            function(tx) {

                var sql = "select e.id, e.name, e.title, e.company, e.location, e.cellphone, e.email " +
                "FROM employee e " +
                "WHERE e.id=?";

            tx.executeSql(sql, [id] ,function(tx, results) {
                    callback(results.rows.length === 1 ? results.rows.item(0) : null);
                });
            },
            function(tx, error) {
                alert("Transaction Error: " + error);
            }
        );
    },

    // Populate Employee table with sample data
    populate: function(callback) {
        directory.db.transaction(
            function(tx) {
                console.log('Dropping EMPLOYEE table');
                tx.executeSql('DROP TABLE IF EXISTS employee');
                var sql =
                    "CREATE TABLE IF NOT EXISTS employee ( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(50), " +
                    "title VARCHAR(50), " +
                    "company VARCHAR(50), " +
                    "location VARCHAR(50), " +
                    "cellPhone VARCHAR(50), " +
                    "email VARCHAR(50)) ";
                console.log('Creating EMPLOYEE table');
                tx.executeSql(sql);
                console.log('Inserting employees');
                tx.executeSql("INSERT INTO employee VALUES (1, 'Ryan Howard', 'Vice President', 'Company' , 'New York, NY', '212-999-8887', 'ryan@dundermifflin.com')");
                tx.executeSql("INSERT INTO employee VALUES (2, 'Michael Scott', 'Regional Manager', 'Company', 'Scranton, PA', '570-123-4567', 'michael@dundermifflin.com')");
                tx.executeSql("INSERT INTO employee VALUES (3, 'Dwight Schrute', 'Assistant Regional Manager', 'Company', 'Scranton, PA', '570-635-1122', 'dwight@dundermifflin.com')");
                tx.executeSql("INSERT INTO employee VALUES (4, 'Jim Halpert', 'Assistant Regional Manager', 'Company', 'Scranton, PA', '570-968-5741', 'jim@dundermifflin.com')");
                tx.executeSql("INSERT INTO employee VALUES (5, 'Pamela Beesly', 'Receptionist', 'Company', 'Scranton, PA', '570-999-7474', 'pam@dundermifflin.com')");
            },
            function(tx, error) {
                alert('Transaction error ' + error);
            },
            function(tx) {
                callback();
            }
        );
    }
});


// Overriding Backbone's sync method. Replace the default RESTful services-based implementation
// with a simple local database approach.
Backbone.sync = function(method, model, options) {

    var dao = new model.dao(directory.db);

    if (method === "read") {
        if (model.id) {
            dao.findById(model.id, function(data) {
                options.success(model, data);
            });
        } else {
            dao.findAll(function(data) {
                options.success(model, data);
            });
        }
    }

};

// -------------------------------------------------- The Models ---------------------------------------------------- //

// The Employee Model
directory.models.Employee = Backbone.Model.extend({

    dao: directory.dao.EmployeeDAO,

    initialize: function() {
        this.reports = new directory.models.EmployeeCollection();
    }

});

// The EmployeeCollection Model
directory.models.EmployeeCollection = Backbone.Collection.extend({

    dao: directory.dao.EmployeeDAO,

    model: directory.models.Employee,

    findByName: function(key) {
        var employeeDAO = new directory.dao.EmployeeDAO(directory.db),
            self = this;
        employeeDAO.findByName(key, function(data) {
            self.reset(data);
        });
    }

});


// -------------------------------------------------- The Views ---------------------------------------------------- //

directory.views.SearchPage = Backbone.View.extend({

    templateLoader: directory.utils.templateLoader,
    EmployeeListView: directory.views.EmployeeListView,

    initialize: function() {
        this.template = _.template(this.templateLoader.get('search-page'));
    },

    render: function(eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        this.listView = new directory.views.EmployeeListView({el: $('ul', this.el), model: this.model});
        this.listView.render();
        return this;
    },

    events: {
        "keyup .search-key": "search"
    },

    search: function(event) {
        var key = $('.search-key').val();
        this.model.findByName(key);
    }
});

directory.views.DirectReportPage = Backbone.View.extend({

    initialize: function() {
        this.template = _.template(directory.utils.templateLoader.get('report-page'));
    },

    render: function(eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        this.listView = new directory.views.EmployeeListView({el: $('ul', this.el), model: this.model});
        this.listView.render();
        return this;
    }

});

directory.views.EmployeeListView = Backbone.View.extend({

    initialize: function() {
        this.model.bind("reset", this.render, this);
    },

    render: function(eventName) {
        $(this.el).empty();
        _.each(this.model.models, function(employee) {
            $(this.el).append(new directory.views.EmployeeListItemView({model: employee}).render().el);
        }, this);
        return this;
    }

});

directory.views.EmployeeListItemView = Backbone.View.extend({

    tagName: "li",

    initialize: function() {
        this.template = _.template(directory.utils.templateLoader.get('employee-list-item'));
    },

    render: function(eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        return this;
    }

});

directory.views.EmployeePage = Backbone.View.extend({

    initialize: function() {
        this.template = _.template(directory.utils.templateLoader.get('employee-page'));
    },

    render: function(eventName) {
        $(this.el).html(this.template(this.model));
        return this;
    }

});

// ----------------------------------------------- The Application Router ------------------------------------------ //

directory.Router = Backbone.Router.extend({

    routes: {
        "": "list",
        "list": "list",
        "employees/:id": "employeeDetails",
        "employees/:id/reports": "directReports"
    },

    initialize: function() {

        var self = this;

        // Keep track of the history of pages (we only store the page URL). Used to identify the direction
        // (left or right) of the sliding transition between pages.
        this.pageHistory = [];

        // Register event listener for back button troughout the app
        $('#content').on('click', '.header-back-button', function(event) {
            window.history.back();
            return false;
        });

        // Check of browser supports touch events...
        if (document.documentElement.hasOwnProperty('ontouchstart')) {
            // ... if yes: register touch event listener to change the "selected" state of the item
            $('#content').on('touchstart', 'a', function(event) {
                self.selectItem(event);
            });
            $('#content').on('touchend', 'a', function(event) {
                self.deselectItem(event);
            });
        } else {
            // ... if not: register mouse events instead
            $('#content').on('mousedown', 'a', function(event) {
                self.selectItem(event);
            });
            $('#content').on('mouseup', 'a', function(event) {
                self.deselectItem(event);
            });
        }

        // We keep a single instance of the SearchPage and its associated Employee collection throughout the app
        this.searchResults = new directory.models.EmployeeCollection();
        this.searchPage = new directory.views.SearchPage({model: this.searchResults});
        this.searchPage.render();
        $(this.searchPage.el).attr('id', 'searchPage');
    },

    selectItem: function(event) {
        $(event.target).addClass('tappable-active');
    },

    deselectItem: function(event) {
        $(event.target).removeClass('tappable-active');
    },

    list: function() {
        var self = this;
        this.slidePage(this.searchPage);
    },

    employeeDetails: function(id) {
        var employee = new directory.models.Employee({id: id}),
            self = this;
        employee.fetch({
            success: function(model, data) {
                self.slidePage(new directory.views.EmployeePage({model: data}).render());
            }
        });
    },

    directReports: function(id) {
        var employee = new directory.models.Employee({id: id});
        employee.reports.fetch();
        this.slidePage(new directory.views.DirectReportPage({model: employee.reports}).render());
    },

    slidePage: function(page) {

        var slideFrom,
            self = this;

        // If there is no current page (app just started) -> No transition: Position new page in the view port
        if (!this.currentPage) {
            $(page.el).attr('class', 'page stage-center');
            $('#content').append(page.el);
            this.pageHistory = [window.location.hash];
            this.currentPage = page;
            return;
        }

        // Cleaning up: remove old pages that were moved out of the viewport
        $('.stage-right, .stage-left').not('#searchPage').remove();

        if (page === this.searchPage) {
            // Always apply a Back (slide from left) transition when we go back to the search page
            slideFrom = "left";
            $(page.el).attr('class', 'page stage-left');
            // Reinitialize page history
            this.pageHistory = [window.location.hash];
        } else if (this.pageHistory.length > 1 && window.location.hash === this.pageHistory[this.pageHistory.length - 2]) {
            // The new page is the same as the previous page -> Back transition
            slideFrom = "left";
            $(page.el).attr('class', 'page stage-left');
            this.pageHistory.pop();
        } else {
            // Forward transition (slide from right)
            slideFrom = "right";
            $(page.el).attr('class', 'page stage-right');
            this.pageHistory.push(window.location.hash);
        }

        $('#content').append(page.el);

        // Wait until the new page has been added to the DOM...
        setTimeout(function() {
            // Slide out the current page: If new page slides from the right -> slide current page to the left, and vice versa
            $(self.currentPage.el).attr('class', 'page transition ' + (slideFrom === "right" ? 'stage-left' : 'stage-right'));
            // Slide in the new page
            $(page.el).attr('class', 'page stage-center transition');
            self.currentPage = page;
        });

    }

});

// Bootstrap the application
directory.db = window.openDatabase("EmployeeDB", "1.0", "Employee Demo DB", 200000);
var employeeDAO = new directory.dao.EmployeeDAO(directory.db);
employeeDAO.populate(function() {
    directory.utils.templateLoader.load(['search-page', 'report-page', 'employee-page', 'employee-list-item'],
        function() {
            directory.app = new directory.Router();
            Backbone.history.start();
        });
});