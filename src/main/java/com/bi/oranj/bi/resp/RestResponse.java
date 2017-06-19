package com.bi.oranj.bi.resp;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Wrap a map to be used as a response in JSON REST Controllers.
 */
public class RestResponse implements Map<String, Object>, BIResponse {

	public static final String EMPTY = "";

	public static final String STATUS_KEY = "status";
	public static final String MESSAGE_KEY = "message";
	public static final String DATA_KEY = "data";

	public static final String ERROR_STATUS = "error";
	public static final String SUCCESS_STATUS = "success";
	public static final String FAIL_STATUS = "fail";
	public static final String RECORDS_TOTAL_KEY = "recordsTotal";
	public static final String RECORDS_FILTERED_KEY = "recordsFiltered";
	public static final String DRAW_KEY = "draw";

	private Map<String, Object> delegate;

	/**
	 * Builds an empty response with no status or message.
	 */
	public RestResponse() {
		this.delegate = new LinkedHashMap<String, Object>();
	}

	/**
	 * Add a new value to the response.
	 *
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 * @return the rest response
	 */
	public RestResponse add(String name, Object value) {
		this.delegate.put(name, value);
		return this;
	}

	/**
	 * Sets message key for this response.
	 *
	 * @param message
	 *            the message
	 * @return the rest response
	 */
	public RestResponse withMessage(String message) {
		return add(MESSAGE_KEY, message);
	}

	/**
	 * Sets this response as error, setting the status and message key.
	 *
	 * @param error
	 *            message
	 * @return the rest response
	 */
	public RestResponse withError(String error) {
		return add(STATUS_KEY, ERROR_STATUS).add(MESSAGE_KEY, error);
	}

	/**
	 * Sets this response as success, setting the status and message key.
	 *
	 * @param message
	 *            the message
	 * @return the rest response
	 */
	public RestResponse withSuccess(String message) {
		return add(STATUS_KEY, SUCCESS_STATUS).add(MESSAGE_KEY, message);
	}

	/**
	 * Sets this response as success.
	 *
	 * @return the rest response
	 */
	public RestResponse withSuccess() {
		return add(STATUS_KEY, SUCCESS_STATUS);
	}

	/**
	 * Sets this response as fail.
	 *
	 * @return the rest response
	 */
	public RestResponse withFail() {
		return add(STATUS_KEY, FAIL_STATUS);
	}

	/**
	 * Creates a success response with data.
	 *
	 * @param message
	 *            the message
	 * @param data
	 *            the data
	 * @return the rest response
	 */
	public RestResponse withSuccess(String message, Object data) {
		return withSuccess(message, DATA_KEY, data);
	}

	/**
	 * Creates a success response with custom data.
	 *
	 * @param message
	 *            the message
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 * @return the rest response
	 */
	public RestResponse withSuccess(String message, String name, Object value) {
		return withSuccess(message).add(name, value);
	}

	/**
	 * Sets object as 'data' value.
	 *
	 * @param data
	 *            the data
	 * @return the rest response
	 */
	public RestResponse withData(Object data) {
		return add(DATA_KEY, data);
	}

	/**
	 * Creates a success response with custom data
	 *
	 * @param message
	 * @param name
	 * @param value
	 * @return
	 */
	public RestResponse withData(Long totalPages, Long totalSize,
			Integer pageNumber, String key, Object value) {
		if (!delegate.containsKey(DATA_KEY)) {
			add(RECORDS_TOTAL_KEY, totalSize)
					.add(RECORDS_FILTERED_KEY, totalPages)
					.add(DRAW_KEY, pageNumber)
					// .add(DATA_KEY, new LinkedHashMap<String, Object>());
					.add(DATA_KEY, value);
		}
		return this;
	}

	/**
	 * With data.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return the rest response
	 */
	@SuppressWarnings("unchecked")
	public RestResponse withData(String key, Object value) {
		if (!delegate.containsKey(DATA_KEY)) {
			add(DATA_KEY, new LinkedHashMap<String, Object>());
		}
		((Map<String, Object>) delegate.get(DATA_KEY)).put(key, value);
		return this;
	}

	/**
	 * Factory method for a success response.
	 *
	 * @return the rest response
	 */
	public static RestResponse success() {
		return new RestResponse().withSuccess();
	}

	/**
	 * Factory method for a fail response.
	 *
	 * @return the rest response
	 */
	public static RestResponse fail() {
		return new RestResponse().withFail();
	}

	/**
	 * Factory method for a success response.
	 *
	 * @param message
	 *            the message
	 * @return the rest response
	 */
	public static RestResponse success(String message) {
		return new RestResponse().withSuccess(message);
	}

	/**
	 * Factory method for a success response.
	 *
	 * @param message
	 *            the message
	 * @param data
	 *            the data
	 * @return the rest response
	 */
	public static RestResponse success(String message, Object data) {
		return RestResponse.success(message, DATA_KEY, data);
	}

	/**
	 * Success2.
	 *
	 * @param message
	 *            the message
	 * @param data
	 *            the data
	 * @return the rest response
	 */
	public static RestResponse success2(String message, Object data) {
		throw new IllegalStateException();
	}

	/**
	 * Success2.
	 *
	 * @return the rest response
	 */
	public static RestResponse success2() {
		throw new IllegalStateException();
	}

	/**
	 * Factory method for success.
	 *
	 * @param message
	 *            the message
	 * @param name
	 *            the name
	 * @param data
	 *            the data
	 * @return the rest response
	 */
	public static RestResponse success(String message, String name, Object data) {
		return new RestResponse().withSuccess(message).add(name, data);
	}

	/**
	 * Factory method for error response.
	 *
	 * @param errorMessage
	 *            the error message
	 * @return the rest response
	 */
	public static RestResponse error(String errorMessage) {
		return new RestResponse().withError(errorMessage);
	}

	/**
	 * Factory method for error response using the exception message.
	 *
	 * @param e
	 *            the exception
	 * @return the rest response
	 */
	public static RestResponse error(Exception e) {
		return new RestResponse().withError(e.getMessage());
	}

	/**
	 * Factory method for missing params errors.
	 *
	 * @param param
	 *            the param
	 * @return the rest response
	 */
	public static RestResponse missingParam(String param) {
		return new RestResponse().withError("Param " + param + " is missing");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return delegate.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return delegate.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value) {
		return delegate.containsValue(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public Object get(Object key) {
		return delegate.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object put(String key, Object value) {
		return delegate.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public Object remove(Object key) {
		return delegate.remove(key);
	}

	/**
	 * Put all fields into the internal map.
	 *
	 * @param m
	 *            the map of fields
	 */
	@Override
	public void putAll(Map<? extends String, ?> m) {
		delegate.putAll(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		delegate.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<String> keySet() {
		return delegate.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<Object> values() {
		return delegate.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<Entry<String, Object>> entrySet() {
		return delegate.entrySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#getOrDefault(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object getOrDefault(Object key, Object defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#forEach(java.util.function.BiConsumer)
	 */
	@Override
	public void forEach(BiConsumer<? super String, ? super Object> action) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#replaceAll(java.util.function.BiFunction)
	 */
	@Override
	public void replaceAll(
			BiFunction<? super String, ? super Object, ? extends Object> function) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#putIfAbsent(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object putIfAbsent(String key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#remove(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean remove(Object key, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#replace(java.lang.Object, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public boolean replace(String key, Object oldValue, Object newValue) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#replace(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object replace(String key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#computeIfAbsent(java.lang.Object,
	 * java.util.function.Function)
	 */
	@Override
	public Object computeIfAbsent(String key,
			Function<? super String, ? extends Object> mappingFunction) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#computeIfPresent(java.lang.Object,
	 * java.util.function.BiFunction)
	 */
	@Override
	public Object computeIfPresent(
			String key,
			BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#compute(java.lang.Object,
	 * java.util.function.BiFunction)
	 */
	@Override
	public Object compute(
			String key,
			BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#merge(java.lang.Object, java.lang.Object,
	 * java.util.function.BiFunction)
	 */
	@Override
	public Object merge(
			String key,
			Object value,
			BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
		// TODO Auto-generated method stub
		return null;
	}
}